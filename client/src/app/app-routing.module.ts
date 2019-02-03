import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './pages/dashboard/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { AnonymousGuard } from './guards/anonymous.guard';
import { ReserveComponent } from './pages/reserve/reserve.component';
import { AppointmentsComponent } from './pages/dashboard/home/appointments/appointments.component';
import { AuthGuard } from './guards/auth.guard';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EmployeeComponentComponent } from './pages/dashboard/home/employee-component/employee-component.component';
import { AdminAppointmentsComponent } from "./pages/dashboard/home/admin-appointments/admin-appointments.component";
import { SettingsComponent } from './pages/settings/settings.component';
import { AccountSettingsComponent } from './pages/settings/account-settings/account-settings.component';
import { ReminderSettingsComponent } from './pages/settings/reminder-settings/reminder-settings.component';
import { AdminServicesComponent } from "./pages/dashboard/home/admin-services/admin-services.component";


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

            { path: 'employee/appts', component: EmployeeComponentComponent},

            { path: 'appointments', component: AppointmentsComponent },

            { path: 'reserve', component: ReserveComponent },
            { path: 'my/appts', component: AppointmentsComponent },
            { path: 'add/employee', component: RegisterComponent },
            { path: 'admin/appts', component: AdminAppointmentsComponent },
            { path: 'admin/services', component: AdminServicesComponent },
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
