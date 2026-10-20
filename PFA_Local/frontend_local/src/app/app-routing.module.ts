// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './services/auth-guard.service';
import { PageNotFoundComponent } from './frontoffice/page-not-found/page-not-found.component';



const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },

  { path: 'home', loadChildren: () => import('./frontoffice/frontoffice.module').then(m => m.FrontofficeModule) },

  {
    path: 'backoffice',
    loadChildren: () => import('./backoffice/backoffice.module').then(m => m.BackofficeModule),
    canActivate: [AuthGuardService],
    data: { expectedRole: 'ADMIN' }
  },

  {
    path: 'manager',
    loadChildren: () => import('./manager/manager.module').then(m => m.ManagerModule),
    canActivate: [AuthGuardService],
    data: { expectedRole: 'MANAGER' }
  },

  {
    path: 'memeber',
    loadChildren: () => import('./memeber/memeber.module').then(m => m.MemeberModule),
    canActivate: [AuthGuardService],
    data: { expectedRole: 'MEMBER' }
  },
{ path: 'forgotPassword', redirectTo: '/home/forgotPassword', pathMatch: 'full' },


{ path: 'resetPassword', redirectTo: '/home/resetPassword', pathMatch: 'full' },



  // 404 globale
  { path: '404', component: PageNotFoundComponent },
  

  // Toujours en dernier : redirection vers l’URL 404 (met à jour l’URL)
  { path: '**', redirectTo: '404' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
