import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashbordComponent } from './dashbord/dashbord.component';



import { ListProjetComponent } from './list-projet/list-projet.component';
import { AddProjetComponent } from './add-projet/add-projet.component';
import { UpdateProjetComponent } from './update-projet/update-projet.component';
import { AuthGuardService } from '../services/auth-guard.service';
import { ViewProjectComponent } from './view-project/view-project.component';
import { ConsulterMonProfilComponent } from './consulter-mon-profil/consulter-mon-profil.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';

import { ForcePasswordGuard } from '../services/force-password-guard.guard';
import { ListTacheComponent } from './list-tache/list-tache.component';
import { CreateTacheComponent } from './create-tache/create-tache.component';
import { UpdateTacheComponent } from './update-tache/update-tache.component';
import { TableauBoardComponent } from './tableau-board/tableau-board.component';








const routes: Routes = [
  {
    path: '',
    component: DashbordComponent, // Le layout avec sidebar + header
    canActivate: [AuthGuardService, ForcePasswordGuard],
    children: [
      { path: '', redirectTo: 'tableauBoard', pathMatch: 'full' },
      { path: 'tableauBoard', component: TableauBoardComponent },
      { path: 'listProjet', component: ListProjetComponent },
      { path: 'addProjet', component: AddProjetComponent },
      { path: 'updateProjet/:numeroProjet', component: UpdateProjetComponent },
      { path: 'listTaches/:numeroProjet', component: ListTacheComponent },
      { path: 'createTache/:numeroProjet', component: CreateTacheComponent },
      { path: 'updateTache/:id/:numeroProjet', component: UpdateTacheComponent },
      { path: 'ViewProjet/:numeroProjet', component: ViewProjectComponent },
      { path: 'consulterMonProfil', component: ConsulterMonProfilComponent },
    ]
  },

  // Routes indépendantes (hors tableau de bord)
  { path: 'updatePassword', component: UpdatePasswordComponent },


  // Redirection par défaut
  { path: '', redirectTo: 'manager', pathMatch: 'full' },
   

 
];



@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManagerRoutingModule { }


