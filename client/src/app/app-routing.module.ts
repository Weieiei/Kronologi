import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './pages/dashboard/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { ReserveComponent } from './pages/dashboard/reserve/reserve.component';
import { AnonymousGuard } from './guards/anonymous/anonymous.guard';
import { AppointmentsComponent } from './pages/dashboard/home/appointments/appointments.component';
import { AuthGuard } from './guards/auth/auth.guard';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { VerifiedComponent } from './pages/verified/verified.component';
import { EmployeeAppointmentsComponent } from './pages/dashboard/home/employee-appointments/employee-appointments.component';
import { AdminAppointmentsComponent } from './pages/dashboard/home/admin-appointments/admin-appointments.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { AccountSettingsComponent } from './pages/settings/account-settings/account-settings.component';
import { ReminderSettingsComponent } from './pages/settings/reminder-settings/reminder-settings.component';
import { EmployeeGuard } from './guards/employee/employee.guard';
import { ReviewComponent } from './pages/dashboard/review/review.component';
import { AdminServicesComponent } from './pages/dashboard/home/admin-services/admin-services.component';
import { CreateServiceComponent } from './pages/create-service/create-service.component';
import { AdminUsersComponent } from './pages/dashboard/home/admin-users/admin-users.component';

import { ShiftComponent } from './pages/dashboard/home/shift/shift.component';
import { AdminGuard } from './guards/admin/admin.guard';
import { AdminEmployeesComponent } from './pages/dashboard/home/admin-employees/admin-employees.component';

const routes: Routes = [
    // Login
    { path: 'login', component: LoginComponent, canActivate: [AnonymousGuard] },

    // Register
    { path: 'register', component: RegisterComponent, canActivate: [AnonymousGuard] },

    { path: 'verification', component: VerifiedComponent, canActivate: [AnonymousGuard] },

    // Dashboard
    {
        path: '',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        children: [

            // Home page
            { path: '', component: HomeComponent },

            // Admin
            { path: 'admin/employees', component: AdminEmployeesComponent, canActivate: [AdminGuard] },
            { path: 'admin/employees/:id/shifts', component: ShiftComponent, canActivate: [AdminGuard] },
            // Reserving and modifying reservation
            {
                path: 'reserve', children: [
                    { path: '', component: ReserveComponent, data: { edit: false } },
                    { path: 'edit/:id', component: ReserveComponent, data: { edit: true } }
                ]
            },

            // Appointments
            { path: 'employee/appts', component: EmployeeAppointmentsComponent, canActivate: [EmployeeGuard] },
            { path: 'appointments', component: AppointmentsComponent },

            { path: 'reserve', component: ReserveComponent },
            { path: 'review/:apptmtId', component: ReviewComponent },
            { path: 'my/appts', component: AppointmentsComponent },
            { path: 'add/employee', component: RegisterComponent },
            { path: 'admin/appts', component: AdminAppointmentsComponent },
            { path: 'admin/services', component: AdminServicesComponent },
            { path: 'admin/services/create', component: CreateServiceComponent },
            { path: 'admin/users', component: AdminUsersComponent },

            // User settings
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
