import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MemeberRoutingModule } from './memeber-routing.module';
import { DashbordComponent } from './dashboard/dashboard.component';
import { ChatComponent } from './chat/chat.component';
import { ConsulterMonProfilComponent } from './consulter-mon-profil/consulter-mon-profil.component';

import { UpdatePasswordComponent } from './update-password/update-password.component';

import { DragDropModule } from '@angular/cdk/drag-drop';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ListProjetComponent } from './list-projet/list-projet.component';
import { ListTacheComponent } from './list-tache/list-tache.component';

import { ViewProjectComponent } from './view-project/view-project.component';
import { PickerComponent } from "@ctrl/ngx-emoji-mart";
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MesTachesComponent } from './mes-taches/mes-taches.component';






@NgModule({
  declarations: [
    DashbordComponent,
    ChatComponent,
    ConsulterMonProfilComponent,
   
    UpdatePasswordComponent,
    ListProjetComponent,
    ListTacheComponent,

    ViewProjectComponent,
      MesTachesComponent,
      
  
    
  ],
  imports: [
    CommonModule,
    MemeberRoutingModule,
     FormsModule,
     ReactiveFormsModule,
     DragDropModule,
     PickerComponent,
       MatSelectModule,
    MatFormFieldModule,
  ]
})
export class MemeberModule { }


    
    
    
     