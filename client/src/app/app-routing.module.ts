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
import { AdminGuard } from './guards/admin.guard';
import { AuthService } from './services/auth/auth.service';

const routes: Routes = [
    { path: '', component: HomeComponent },

    { path: 'home', redirectTo: '', pathMatch: 'full' },
    { path: 'appointments', component: AppointmentsComponent, canActivate: [AuthGuard, AdminGuard] },

    { path: 'login', component: LoginComponent, canActivate: [AnonymousGuard] },
    { path: 'register', component: RegisterComponent, canActivate: [AnonymousGuard], data: {
            type: AuthService.registerClient,
            header: 'Sign Up for an Account',
            button: 'Create Account'
        }
    },

    { path: 'reserve', component: ReserveComponent, canActivate: [AuthGuard] },
    { path: 'my/appts', component: MyAppointmentsComponent, canActivate: [AuthGuard] },

    { path: 'add/employee', component: RegisterComponent, canActivate: [AuthGuard, AdminGuard], data: {
            type: AuthService.registerEmployee,
            header: 'Create an Employee Account',
            button: 'Create Employee'
        }
    }
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
