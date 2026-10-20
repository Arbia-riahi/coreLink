
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ManagerRoutingModule } from './manager-routing.module';
import { DashbordComponent } from './dashbord/dashbord.component';
import { ListProjetComponent } from './list-projet/list-projet.component';
import { UpdateProjetComponent } from './update-projet/update-projet.component';
import { AddProjetComponent } from './add-projet/add-projet.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ViewProjectComponent } from './view-project/view-project.component';
import { ChatComponent } from './chat/chat.component';
import { ConsulterMonProfilComponent } from './consulter-mon-profil/consulter-mon-profil.component';

import { UpdatePasswordComponent } from './update-password/update-password.component';

import { PickerComponent } from "@ctrl/ngx-emoji-mart";
import { CreateTacheComponent } from './create-tache/create-tache.component';
import { UpdateTacheComponent } from './update-tache/update-tache.component';
import { ListTacheComponent } from './list-tache/list-tache.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TableauBoardComponent } from './tableau-board/tableau-board.component';
import { NgxChartsModule } from '@swimlane/ngx-charts';













@NgModule({
  declarations: [
    DashbordComponent,
    ListProjetComponent,
    UpdateProjetComponent,
    AddProjetComponent,
    ViewProjectComponent,
    ChatComponent,
    ConsulterMonProfilComponent,
    
    UpdatePasswordComponent,
      
          CreateTacheComponent,
          UpdateTacheComponent,
          ListTacheComponent,
          TableauBoardComponent,
      
       
        
  ],
  imports: [
    CommonModule,
    ManagerRoutingModule,
    FormsModule,
    PickerComponent,
     ReactiveFormsModule, // Obligatoire pour formGroup
      DragDropModule ,      // Obligatoire pour cdkDropList
       MatSelectModule,
    MatFormFieldModule,
    NgxChartsModule
    

   
]
})
export class ManagerModule { }


