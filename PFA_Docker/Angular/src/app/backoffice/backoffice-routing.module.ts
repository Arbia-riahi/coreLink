import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashbordComponent } from './dashboard/dashboard.component';
import { AuthGuardService } from '../services/auth-guard.service';
import { ListActualiteComponent } from './list-actualite/list-actualite.component';
import { AddActualiteComponent } from './add-actualite/add-actualite.component';
import { UpdateActualiteComponent } from './update-actualite/update-actualite.component';
import { ListInscriptionComponent } from './list-inscription/list-inscription.component';
import { AddInscriptionComponent } from './add-inscription/add-inscription.component';
import { UpdateInscriptionComponent } from './update-inscription/update-inscription.component';
import { ForcePasswordGuard } from '../services/force-password-guard.guard';
import { UpdatePasswordComponent } from './update-password/update-password.component';
import { ListProjetComponent } from './list-projet/list-projet.component';
import { TableauBoardComponent } from './tableau-board/tableau-board.component';
import { ConsulterMonProfilComponent } from './consulter-mon-profile/consulter-mon-profile.component';



const routes: Routes = [
  {

    path: '',
    component: DashbordComponent,
    canActivate: [AuthGuardService, ForcePasswordGuard],
    children: [
      { path: '', redirectTo: 'tableauBoard', pathMatch: 'full' },
      { path: 'tableauBoard', component: TableauBoardComponent},
      {
        path: 'listInscription',
        component: ListInscriptionComponent
      },
      {
        path: 'addInscription',
        component: AddInscriptionComponent
      },
       {
        path: 'updateInscription/:matricule',
        component: UpdateInscriptionComponent
      },
     
     
      
    
      {
        path: 'listActualite',
        component: ListActualiteComponent
      }
      ,
      {
        path: 'addActualite',
        component: AddActualiteComponent
      },
     
      {
        path: 'updateActualite/:id',
        component: UpdateActualiteComponent
      },
 {
        path: 'listProjet',
        component: ListProjetComponent
      },
      
      { path: 'consulterMonProfil', component: ConsulterMonProfilComponent},
      
     



   
     
      


     
      
    ]
    
  },
  
      { path: 'updatePassword', component: UpdatePasswordComponent },

      
      // Redirection par défaut
  { path: '', redirectTo: 'backoffice', pathMatch: 'full' },



];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackofficeRoutingModule { }

