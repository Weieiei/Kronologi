import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './pages/dashboard/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { AnonymousGuard } from './guards/anonymous/anonymous.guard';
import { ReserveComponent } from './pages/reserve/reserve.component';
import { AppointmentsComponent } from './pages/dashboard/home/appointments/appointments.component';
import { AuthGuard } from './guards/auth/auth.guard';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EmployeeAppointmentsComponent } from './pages/dashboard/home/employee-appointments/employee-appointments.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { AccountSettingsComponent } from './pages/settings/account-settings/account-settings.component';
import { ReminderSettingsComponent } from './pages/settings/reminder-settings/reminder-settings.component';
import { EmployeeGuard } from './guards/employee/employee.guard';
import { ReviewComponent } from './pages/dashboard/review/review.component';
const routes: Routes = [
    // Login
    { path: 'login', component: LoginComponent, canActivate: [AnonymousGuard] },

    // Register
    { path: 'register', component: RegisterComponent, canActivate: [AnonymousGuard] },

    // Dashboard
    {
        path: '',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        children: [

            // Home page
            { path: '', component: HomeComponent },

            // Appointments
            { path: 'employee/appts', component: EmployeeAppointmentsComponent, canActivate: [EmployeeGuard] },
            { path: 'appointments', component: AppointmentsComponent },

            { path: 'reserve', component: ReserveComponent },
            { path: 'review/:apptmtId', component: ReviewComponent },
            { path: 'my/appts', component: AppointmentsComponent },
            { path: 'add/employee', component: RegisterComponent },
            {
                path: 'settings', component: SettingsComponent, children: [
                    { path: '', redirectTo: 'account', pathMatch: 'full' },
                    { path: 'account', component: AccountSettingsComponent },
                    { path: 'reminders', component: ReminderSettingsComponent }
                ]
            }
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
