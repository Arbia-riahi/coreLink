import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BackofficeRoutingModule } from './backoffice-routing.module';
import { DashbordComponent } from './dashboard/dashboard.component';

import { ListActualiteComponent } from './list-actualite/list-actualite.component';
import { AddActualiteComponent } from './add-actualite/add-actualite.component';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import { UpdateActualiteComponent } from './update-actualite/update-actualite.component';
import { ListInscriptionComponent } from './list-inscription/list-inscription.component';
import { AddInscriptionComponent } from './add-inscription/add-inscription.component';
import { UpdateInscriptionComponent } from './update-inscription/update-inscription.component';
import { ChatComponent } from './chat/chat.component';

import { UpdatePasswordComponent } from './update-password/update-password.component';


import { TableauBoardComponent } from './tableau-board/tableau-board.component';
import { ListProjetComponent } from './list-projet/list-projet.component';







import { PickerComponent } from "@ctrl/ngx-emoji-mart";

import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';

import { NgxChartsModule } from '@swimlane/ngx-charts';
import { ConsulterMonProfilComponent } from './consulter-mon-profile/consulter-mon-profile.component';








@NgModule({
  declarations: [
   
 DashbordComponent,
  
   
    
    ListActualiteComponent,
    AddActualiteComponent,
    UpdateActualiteComponent,
   
      
          
          ListInscriptionComponent,
                              AddInscriptionComponent,
                              UpdateInscriptionComponent,
                              ChatComponent,
                             
                              UpdatePasswordComponent,
                             
                           
                              TableauBoardComponent,
                              ListProjetComponent,
                              ConsulterMonProfilComponent,
                           
                          
        

  ],
  imports: [
    CommonModule,
    BackofficeRoutingModule,
    FormsModule,
     CommonModule,
    
       
      
        PickerComponent,
         ReactiveFormsModule, // Obligatoire pour formGroup
          DragDropModule ,      // Obligatoire pour cdkDropList
           MatSelectModule,
        MatFormFieldModule,
        NgxChartsModule
  ],
  providers: [

   
  ],
})
export class BackofficeModule { }
