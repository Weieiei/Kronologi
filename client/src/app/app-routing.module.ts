import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { AnonymousGuard } from './guards/anonymous.guard';
import { AppointmentsComponent } from './components/appointments/appointments.component';
import { ReserveComponent } from './components/reserve/reserve.component';
import { MyAppointmentsComponent } from './components/my-appointments/my-appointments.component';
import { AuthGuard } from './guards/auth.guard';

export interface Data {
  type?: string;
  header?: string;
  button?: string;
}

const routes: Routes = [
  { path: '', component: HomeComponent },

  { path: 'home', redirectTo: '', pathMatch: 'full' },
  { path: 'appointments', component: AppointmentsComponent, canActivate: [AuthGuard] },

  { path: 'login', component: LoginComponent, canActivate: [AnonymousGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [AnonymousGuard], data: {
      type: 'register-client',
      header: 'Sign Up for an Account',
      button: 'Create Account'
} },

  { path: 'reserve', component: ReserveComponent, canActivate: [AuthGuard] },
  { path: 'my/appts', component: MyAppointmentsComponent, canActivate: [AuthGuard] },

  { path: 'add/employee', component: RegisterComponent, canActivate: [AuthGuard], data: {
      type: 'register-employee',
      header: 'Create an Employee Account',
      button: 'Create Employee'
    } }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  declarations: []
})
export class AppRoutingModule { }
