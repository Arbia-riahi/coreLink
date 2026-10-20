import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  ElementRef,
  AfterViewInit
} from '@angular/core';
import { ChatService } from '../../services/chat.service';
import { Subscription } from 'rxjs';
import { UserService } from '../../services/user.service';
import { environment } from '../../../environments/environment.development';

declare var EmojiMart: any;

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, AfterViewInit, OnDestroy {
  messages: any[] = [];
  onlineUsers: any[] = [];
  newMessage = '';
  userData: any;
  isConnected = false;
  isOpen = false;
  unreadCount = 0;

  private subscriptions: Subscription[] = [];
  private userIsScrolling = false;
  readonly baseUrlImage = `${environment.baseUrl}uploads/files/`;

  @ViewChild('emojiPickerContainer', { static: false })
  emojiPickerContainer!: ElementRef;
  emojiPicker: any;
  showEmojiPicker = false;

  @ViewChild('messagesContainer')
  messagesContainer!: ElementRef;

  // Gestion des fichiers
  selectedFile: File | null = null;
  filePreviewUrl: string | ArrayBuffer | null = null;

  // Gestion "vu par"
  seenByUsers: any[] = [];
  selectedMessageId: number | null = null;
  selectedMessageInfo = { time: new Date(), seenBy: [] as any[] };

  // Gestion audio
  isRecording = false;
  mediaRecorder: MediaRecorder | null = null;
  audioChunks: Blob[] = [];
  audioBlob: Blob | null = null;
  audioUrl: string | null = null;
  hoveredMessage: any = null;


reactionTimeout: any = null;






  constructor(private chatService: ChatService, private userService: UserService) {}

  ngOnInit(): void {
    this.chatService.connect();
    this.loadUserData();
    this.loadMessageHistory();

    if (this.isOpen) {
      setTimeout(() => this.markMessagesAsRead(), 1000);
    } else {
      setTimeout(() => this.loadUnreadCount(), 1000);
    }

    // Connexion WebSocket
    this.subscriptions.push(
      this.chatService.getConnectionStatus().subscribe(status => (this.isConnected = status))
    );

    // Réception des messages
    this.subscriptions.push(
      this.chatService.getMessages().subscribe(msg => {
        if (msg) {
          this.messages.push(this.formatMessage(msg));
          this.scrollToBottom();
          if (!this.isOpen) {
            this.loadUnreadCount();
          } else {
            this.markMessagesAsRead();
          }
        }
      })
    );

    // Réactions
 
 this.subscriptions.push(
  this.chatService.getReactions().subscribe(reaction => {
    const msg = this.messages.find(m => m.id === reaction.messageId);
    if (!msg) return;

    if (!msg.reactions) msg.reactions = [];

    const userFullName = `${reaction.user.nom} ${reaction.user.prenom}`;

    // Supprimer s'il existe déjà
   msg.reactions = msg.reactions.filter(
  (r: any) => !(r.emoji === reaction.emoji && r.user?.matricule === reaction.user.matricule)
);
    // Ajouter si pas "removed"
    if (!reaction.removed) {
      msg.reactions.push({
      emoji: reaction.emoji,
    user: reaction.user
      });
    }

    this.messages = [...this.messages]; // force l’UI à se rafraîchir
  })
);



    
    // Mises à jour "vu"
    this.subscriptions.push(
      this.chatService.getSeenUpdates().subscribe(update => {
        const msg = this.messages.find(m => m.id === update.messageId);
        if (msg) {
          msg.seenBy = update.seenBy;
          this.messages = [...this.messages]; // Force Angular refresh
        }
      })
    );

    // Utilisateurs en ligne
    this.subscriptions.push(
      this.chatService.getOnlineUsers().subscribe(users => {
        this.onlineUsers = users.map(u => this.formatUser(u));
      })
    );
  }


selectMessage(id: number): void {
  console.log("Message cliqué :", id); // debug temporaire
  this.selectedMessageId = this.selectedMessageId === id ? null : id;
}


  ngAfterViewInit(): void {
    if (this.messagesContainer) {
      this.messagesContainer.nativeElement.addEventListener('scroll', () => {
        const container = this.messagesContainer.nativeElement;
        this.userIsScrolling =
          container.scrollHeight - container.scrollTop - container.clientHeight > 50;
      });
    }
  }

  /** Gestion de l'historique */
  loadMessageHistory(): void {
    this.chatService.getMessageHistory().subscribe(history => {
      this.messages = history.map(msg => this.formatMessage(msg));
      this.scrollToBottom(true);
    });
  }

  /** Chargement utilisateur */
  loadUserData(): void {
    const matricule = localStorage.getItem('matricule');
    if (matricule) {
      this.userService.getUserByMatricule(matricule).subscribe(user => {
        this.userData = user;
        this.loadUnreadCount();
      });
    }
  }

  /** Marquer tous les messages comme lus */
  markMessagesAsRead(): void {
    if (!this.userData) return;

    this.messages.forEach(msg => {
      if (msg.sender?.matricule !== this.userData.matricule) {
        this.chatService.markMessageAsRead(msg.id, this.userData.matricule).subscribe(() => {
          if (!msg.seenBy) msg.seenBy = [];
          const alreadySeen = msg.seenBy.some((u: any) => u.matricule === this.userData.matricule);
          if (!alreadySeen) {
            msg.seenBy.push({
              matricule: this.userData.matricule,
              nom: this.userData.nom,
              prenom: this.userData.prenom
            });
            this.messages = [...this.messages]; // refresh UI
          }
        });
      }
    });

    this.unreadCount = 0;
  }

  /** Clic sur un message pour afficher menu réactions */
 onMessageClick(msg: any): void {
  // Ferme les autres infos
  this.messages.forEach(m => {
    if (m.id !== msg.id) m.showInfo = false;
  });

  // Toggle celui-ci
  msg.showInfo = !msg.showInfo;
  this.selectedMessageId = msg.showInfo ? msg.id : null;
  this.selectedMessageInfo = { time: msg.timestamp, seenBy: msg.seenBy || [] };
}

  



onMouseEnterMessage(msg: any): void {
  clearTimeout(this.reactionTimeout); // stop délai fermeture
  this.hoveredMessage = msg;
}

onMouseLeaveMessage(): void {
  this.reactionTimeout = setTimeout(() => {
    this.hoveredMessage = null;
  }, 300); // attend 300ms avant de cacher
}




/** Ajout ou suppression d'une réaction (toggle) */
addReaction(msg: any, emoji: string): void {
  if (!this.userData) return;

  const alreadyReacted = msg.reactions.some(
    (r: any) => r.emoji === emoji && r.user?.matricule === this.userData.matricule
  );

  const reactionPayload = {
    messageId: msg.id,
    emoji,
    user: {
      matricule: this.userData.matricule,
      nom: this.userData.nom,
      prenom: this.userData.prenom,
      image: this.userData.image
    },
    removed: alreadyReacted
  };

  // Envoi aux deux (REST pour persistence, WS pour temps réel)
  this.chatService.sendReactionWebSocket(reactionPayload);
  this.chatService.saveReactionREST(msg.id, reactionPayload).subscribe();

  msg.showReactions = false;
}



  /** Gestion des réactions */
 getUniqueReactions(msg: any): string[] {
  return Array.from(new Set(msg.reactions.map((r: any) => r.emoji)));
}


  countReactions(msg: any, emoji: string): number {
    return msg.reactions.filter((r: any) => r.emoji === emoji).length;
  }




 showReactionUsers(msg: any, emoji: string): void {
  const users = msg.reactions
    .filter((r: any) => r.emoji === emoji)
    .map((r: any) => `${r.user.nom} ${r.user.prenom}`);

  if (users.length === 0) {
    alert(`Aucun utilisateur n’a réagi avec ${emoji}.`);
  } else {
    alert(`${emoji} ajouté par :\n${users.join('\n')}`);
  }
}


  /** Envoi message texte ou fichier */
  sendMessage(): void {
    if ((!this.newMessage.trim() && !this.selectedFile) || !this.userData) return;

    const messagePayload = {
      sender: this.formatUser(this.userData),
      content: this.newMessage,
      timestamp: new Date(),
      fileUrl: ''
    };

    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      this.chatService.uploadFile(formData).subscribe((fileName: string) => {
        messagePayload.fileUrl = fileName;
        this.chatService.sendMessage(messagePayload);
        this.resetInputs();
      });
    } else {
      this.chatService.sendMessage(messagePayload);
      this.resetInputs();
    }
  }

  resetInputs(): void {
    this.newMessage = '';
    this.selectedFile = null;
    this.filePreviewUrl = null;
  }

  /** Gestion non lus */
  loadUnreadCount(): void {
    if (!this.userData) return;
    this.chatService.getUnreadCount(this.userData.matricule).subscribe(count => {
      this.unreadCount = count;
    });
  }

  /** Ouverture / fermeture */
  toggleChat(): void {
    this.isOpen = !this.isOpen;
    if (this.isOpen) {
      this.scrollToBottom(true);
      this.markMessagesAsRead();
      this.unreadCount = 0;
    }
  }

  /** Fichiers */
  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    if (this.selectedFile && this.isImage(this.selectedFile.name)) {
      const reader = new FileReader();
      reader.onload = e => (this.filePreviewUrl = reader.result);
      reader.readAsDataURL(this.selectedFile);
    } else {
      this.filePreviewUrl = null;
    }
  }

  cancelSelectedFile(): void {
    this.selectedFile = null;
    this.filePreviewUrl = null;
  }

  /** Emojis */
  toggleEmojiPicker(): void {
    this.showEmojiPicker = !this.showEmojiPicker;
    if (this.showEmojiPicker && !this.emojiPicker) {
      this.emojiPicker = new EmojiMart.Picker({
        onEmojiSelect: (emoji: any) => (this.newMessage += emoji.native),
        previewPosition: 'none',
        theme: 'light'
      });
      this.emojiPickerContainer.nativeElement.appendChild(this.emojiPicker);
    }
    this.emojiPickerContainer.nativeElement.style.display = this.showEmojiPicker ? 'block' : 'none';
  }

  /** Scroll intelligent */
  scrollToBottom(force: boolean = false): void {
    setTimeout(() => {
      if (this.messagesContainer?.nativeElement) {
        const container = this.messagesContainer.nativeElement;
        const isAtBottom =
          container.scrollHeight - container.scrollTop - container.clientHeight < 5;
        if (force || isAtBottom) {
          container.scrollTop = container.scrollHeight;
        }
      }
    }, 100);
  }

  /** Utilitaires */
  isImage(url: string): boolean {
    return /\.(jpg|jpeg|png|gif)$/i.test(url);
  }

  isVideo(url: string): boolean {
    return /\.(mp4|webm|ogg)$/i.test(url);
  }

  trackByMessage(index: number, msg: any): string {
    return msg.id ? String(msg.id) : msg.timestamp + msg.content;
  }

  getDownloadUrl(fileUrl: string): string {
    return fileUrl ? `${environment.baseUrl}${fileUrl}` : '';
  }

  getFileName(fileUrl: string): string {
    return fileUrl ? decodeURIComponent(fileUrl.split('/').pop() || 'fichier') : '';
  }

private formatMessage(msg: any): any {
  return {
    ...msg,
    sender: this.formatUser(msg.sender),
    reactions: msg.reactions || [],
    seenBy: msg.seenBy || [],
    showInfo: false // <--- pour toggle des infos
  };
}



  private formatUser(user: any): any {
    return {
      ...user,
      image: user?.image
        ? user.image.startsWith('http')
          ? user.image
          : this.baseUrlImage + user.image
        : ''
    };
  }

  /** Audio */
  startRecording(): void {
    if (typeof MediaRecorder === 'undefined') {
      alert('Votre navigateur ne supporte pas l’enregistrement audio.');
      return;
    }

    this.isRecording = true;
    navigator.mediaDevices.getUserMedia({ audio: true })
      .then(stream => {
        this.mediaRecorder = new MediaRecorder(stream, { mimeType: 'audio/webm;codecs=opus' });
        this.audioChunks = [];
        this.mediaRecorder.start();

        this.mediaRecorder.ondataavailable = (e: BlobEvent) => {
          if (e.data.size > 0) {
            this.audioChunks.push(e.data);
          }
        };

        this.mediaRecorder.onstop = () => {
          this.audioBlob = new Blob(this.audioChunks, { type: 'audio/webm;codecs=opus' });
          this.audioUrl = URL.createObjectURL(this.audioBlob);
        };
      })
      .catch(error => {
        console.error('Erreur accès micro :', error);
        this.isRecording = false;
      });
  }

  stopRecording(): void {
    if (this.mediaRecorder && this.isRecording) {
      this.mediaRecorder.stop();
      this.isRecording = false;
    }
  }

  sendVoiceMessage(): void {
    if (!this.audioBlob || this.audioBlob.size === 0) {
      console.error('Audio vide !');
      return;
    }

    const formData = new FormData();
    const extension = this.audioBlob.type.includes('mp3') ? 'mp3' : 'webm';
    formData.append('file', this.audioBlob, `voice_message.${extension}`);

    this.chatService.uploadAudio(formData).subscribe((fileName: string) => {
      const messagePayload = {
        sender: this.userData,
        content: '',
        fileUrl: '',
        audioUrl: fileName,
        timestamp: new Date()
      };

      this.chatService.sendMessage(messagePayload);
      this.audioBlob = null;
      this.audioUrl = null;
      this.audioChunks = [];
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
    this.chatService.disconnect();
  }
}