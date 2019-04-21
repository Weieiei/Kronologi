import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './pages/dashboard/home/home.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
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
import { BusinessRegisterComponent } from './pages/register/business-register/business-register.component';
import { SyncCalendarsComponent } from './pages/sync-calendars/sync-calendars.component';
import { ShiftComponent } from './pages/dashboard/home/shift/shift.component';
import { AdminGuard } from './guards/admin/admin.guard';
import { AdminEmployeesComponent } from './pages/dashboard/home/admin-employees/admin-employees.component';
import { BusinessViewComponent } from './pages/business-view/business-view.component';
import { BookComponent } from './pages/dashboard/reserve/book/book.component';
import { DetailsComponent } from './pages/details/details.component';
import { ReceiptsComponent } from "./pages/receipts/receipts.component";

const routes: Routes = [
    // Login
    { path: 'login', component: LoginComponent },

    // Register
    { path: 'register', component: RegisterComponent, canActivate: [AnonymousGuard] },
    { path: 'register_business', component: BusinessRegisterComponent, canActivate: [AnonymousGuard] },

    { path: 'verification', component: VerifiedComponent, canActivate: [AnonymousGuard] },
    //
    // // Guest
    // { path: 'guest_appointment', component: GuestAppointmentComponent, canActivate: [AnonymousGuard] },
    // { path: 'business', component: BusinessViewComponent, canActivate: [AnonymousGuard] },
    // { path: 'appointments', component: AppointmentsComponent, canActivate: [AnonymousGuard] },
    // { path: 'book/:businessId', component: BookComponent },

    // Dashboard
    {
        path: '',
        component: DashboardComponent,
        // canActivate: [AuthGuard],
        children: [

            // Appointments for business
            { path: 'business', component: BusinessViewComponent},

            // Home
            { path: 'home/:businessId', component: HomeComponent,  canActivate: [AdminGuard] },

            // Admin
            { path: 'admin/employees', component: AdminEmployeesComponent, canActivate: [AdminGuard] },
            { path: 'admin/employees/:id/shifts', component: ShiftComponent, canActivate: [AdminGuard] },
            { path: 'admin/receipts', component: ReceiptsComponent, canActivate: [AdminGuard]},

            // Reserving and modifying reservation
            {path : 'details/:businessId', component: DetailsComponent},
            {path : 'syncCalendars', component: SyncCalendarsComponent, canActivate: [EmployeeGuard]},

            // Appointments
            { path: 'employee/appts', component: EmployeeAppointmentsComponent, canActivate: [EmployeeGuard] },
            { path: 'appointments', component: AppointmentsComponent },
            { path: 'review/:apptmtId', component: ReviewComponent },
            { path: 'my/appts', component: AppointmentsComponent },
            { path: 'add/employee', component: RegisterComponent },
            { path: 'admin/appts', component: AdminAppointmentsComponent },
            { path: 'admin/services', component: AdminServicesComponent },
            { path: 'admin/services/create', component: CreateServiceComponent },
            { path: 'admin/users', component: AdminUsersComponent },
            { path: 'book/:businessId', component: BookComponent },

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
