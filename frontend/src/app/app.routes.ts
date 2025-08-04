import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AdminLoginComponent } from './components/admin-login/admin-login.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { AuthRefreshComponent } from './components/auth-refresh/auth-refresh.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: AdminLoginComponent },
  { path: 'auth/refresh', component: AuthRefreshComponent },
  { 
    path: 'dashboard', 
    component: AdminDashboardComponent,
    canActivate: [AuthGuard]
  },
  { path: '**', redirectTo: '' }
];