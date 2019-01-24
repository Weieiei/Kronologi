import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './pages/dashboard/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { AnonymousGuard } from './guards/anonymous.guard';
import { ReserveComponent } from './pages/reserve/reserve.component';
import { ReviewComponent } from './pages/review/review.component';
import { AppointmentsComponent } from './pages/dashboard/home/appointments/appointments.component';
import { AuthGuard } from './guards/auth.guard';
import { AuthService } from './services/auth/auth.service';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EmployeeComponentComponent } from './pages/dashboard/home/employee-component/employee-component.component';

const routes: Routes = [
    // Login
    {path: 'login', component: LoginComponent, canActivate: [AnonymousGuard]},

    // Register
    {
        path: 'register', component: RegisterComponent, canActivate: [AnonymousGuard], data: {
            type: AuthService.registerClient,
            header: 'Sign Up for an Account',
            button: 'Create Account'
        }
    },

    // Dashboard
    {
        path: '',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        children: [

            // Home page
            {path: '', component: HomeComponent},

            // Appointments
            {path: 'appointments', component: AppointmentsComponent},
            {path: 'employee/appts', component: EmployeeComponentComponent},
            {path: 'reserve', component: ReserveComponent},
            {path: 'my/appts', component: AppointmentsComponent},
            {path: 'review', component: ReviewComponent, canActivate: [AuthGuard] },
            {path: 'add/employee', component: RegisterComponent}
        ]
    },
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
export class AppRoutingModule {
}
