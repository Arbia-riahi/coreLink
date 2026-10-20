import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashbordComponent } from './dashboard/dashboard.component';
import { AuthGuardService } from '../services/auth-guard.service';

import { ConsulterMonProfilComponent } from './consulter-mon-profil/consulter-mon-profil.component';


import { ForcePasswordGuard } from '../services/force-password-guard.guard';
import { UpdatePasswordComponent } from './update-password/update-password.component';
import { ListProjetComponent } from './list-projet/list-projet.component';
import { ListTacheComponent } from './list-tache/list-tache.component';

import { ViewProjectComponent } from './view-project/view-project.component';
import { MesTachesComponent } from './mes-taches/mes-taches.component';


const routes: Routes = [
  {

 path: '',
  component: DashbordComponent,
  canActivate: [AuthGuardService, ForcePasswordGuard],
  children: [
    
     { path: '', redirectTo: 'listProjet', pathMatch: 'full' },
      
      { path: 'consulterMonProfil', component: ConsulterMonProfilComponent },
      { path: 'listProjet', component: ListProjetComponent },
    


    { path: 'listTaches/:numeroProjet', component: ListTacheComponent },
    { path: 'mesTaches/:numeroProjet', component: MesTachesComponent},

    { path: 'ViewProjet/:numeroProjet', component: ViewProjectComponent },
    ]


  },

  { path: 'updatePassword', component: UpdatePasswordComponent },
  
  { path: '', redirectTo: 'memeber', pathMatch: 'full' },
  
 



];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MemeberRoutingModule { }

