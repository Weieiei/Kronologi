import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { CovalentFileModule  } from '@covalent/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NavbarComponent } from './components/navbar/navbar.component';
import { RegisterComponent } from './pages/register/register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthService } from './services/auth/auth.service';
import { HomeComponent } from './pages/dashboard/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './guards/auth/auth.guard';
import { AnonymousGuard } from './guards/anonymous/anonymous.guard';
import { UrlInterceptor } from './interceptor';
import { ReserveComponent } from './pages/dashboard/reserve/reserve.component';
import { AppointmentsComponent } from './pages/dashboard/home/appointments/appointments.component';
import { SchedulerComponent } from './pages/dashboard/reserve/scheduler/scheduler.component';
import { CalendarComponent } from './pages/dashboard/reserve/scheduler/calendar/calendar.component';
import { MonthPickerComponent } from './pages/dashboard/reserve/scheduler/month-picker/month-picker.component';
import { AdminGuard } from './guards/admin/admin.guard';
import { CustomStepperComponent } from './components/custom-stepper/custom-stepper.component';
import { ShiftPickerComponent } from './components/shift-picker/shift-picker.component';
import { TimePickerDialogComponent } from './components/shift-picker/time-picker-dialog/time-picker-dialog.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AppointmentCardComponent } from './pages/dashboard/home/appointments/appointments-section/appointment-card/appointment-card.component';
import { FabBottomRightDirective } from './directives/fab-bottom-right/fab-bottom-right.directive';
import { ButtonIconDirective } from './directives/button-icon/button-icon.directive';
import { AppointmentsSectionComponent } from './pages/dashboard/home/appointments/appointments-section/appointments-section.component';
import { CardSizeDirective } from './directives/card-size/card-size.directive';
import { AdminAppointmentsComponent } from "./pages/dashboard/home/admin-appointments/admin-appointments.component";
import { VerifiedComponent } from './pages/verified/verified.component';
import { EmployeeAppointmentsComponent } from './pages/dashboard/home/employee-appointments/employee-appointments.component';
import { AutofocusDirective } from './directives/autofocus/autofocus.directive';
import { SettingsComponent } from './pages/settings/settings.component';
import { AccountSettingsComponent } from './pages/settings/account-settings/account-settings.component';
import { ReminderSettingsComponent } from './pages/settings/reminder-settings/reminder-settings.component';
import { ShiftComponent } from './pages/dashboard/home/shift/shift.component';
import { CancelDialogComponent } from './components/cancel-dialog/cancel-dialog.component';
import { EmployeePickerComponent } from './pages/dashboard/reserve/employee-picker/employee-picker.component';
import { ServiceSelectionComponent } from './pages/dashboard/reserve/service-selection/service-selection.component';
import { TimePickerComponent } from './pages/dashboard/reserve/time-picker/time-picker.component';
import { NotesAndReserveComponent } from './pages/dashboard/reserve/notes-and-reserve/notes-and-reserve.component';
import { ReviewComponent } from './pages/dashboard/review/review.component';
import { AdminEmployeesComponent } from './pages/dashboard/home/admin-employees/admin-employees.component';
import { AddShiftFormComponent } from './pages/dashboard/home/shift/add-shift-form/add-shift-form.component';
import { AdminServicesComponent } from "./pages/dashboard/home/admin-services/admin-services.component";
import { CreateServiceComponent } from "./pages/create-service/create-service.component";
import { AdminUsersComponent } from "./pages/dashboard/home/admin-users/admin-users.component";
import { AssignServicesDialogComponent } from "./pages/dashboard/home/admin-users/assign-services-dialog/assign-services-dialog.component";
import { ChangeClientToEmployeeDialogComponent } from "./pages/dashboard/home/admin-users/change-client-to-employee-dialog/change-client-to-employee-dialog.component";
import { ReasonDialogComponent } from './components/reason-dialog/reason-dialog.component';
import { BusinessViewComponent } from './pages/business-view/business-view.component';
import { CardsUiComponent } from './components/cards-ui/cards-ui.component';

import { BusinessRegisterComponent } from './pages/register/business-register/business-register.component';
import { NgxSpinnerModule } from 'ngx-spinner';
import { ServicesComponent } from './pages/services/services.component';
import { SyncCalendarsComponent } from './pages/sync-calendars/sync-calendars.component';
import { FindBusinessDialogComponent } from './components/find-business-dialog/find-business-dialog.component';
import { AgmCoreModule } from '@agm/core';
import { ErrorDialogComponent } from './components/error-dialog/error-dialog.component';
import { DetailsComponent } from './pages/details/details.component';
import { GoogleMapsComponent } from './components/google-maps/google-maps.component';

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        RegisterComponent,
        HomeComponent,
        LoginComponent,
        ReserveComponent,
        AppointmentsComponent,
        SchedulerComponent,
        CalendarComponent,
        MonthPickerComponent,
        CustomStepperComponent,
        ShiftPickerComponent,
        TimePickerDialogComponent,
        DashboardComponent,
        AppointmentCardComponent,
        FabBottomRightDirective,
        ButtonIconDirective,
        AppointmentsSectionComponent,
        AdminAppointmentsComponent,
        CardSizeDirective,
        VerifiedComponent,
        EmployeeAppointmentsComponent,
        AutofocusDirective,
        SettingsComponent,
        AccountSettingsComponent,
        ChangeClientToEmployeeDialogComponent,
        ReviewComponent,
        ReminderSettingsComponent,
        ShiftComponent,
        AdminEmployeesComponent,
        AdminServicesComponent,
        CreateServiceComponent,
        AdminUsersComponent,
        AssignServicesDialogComponent,
        ChangeClientToEmployeeDialogComponent,
        EmployeePickerComponent,
        ServiceSelectionComponent,
        TimePickerComponent,
        NotesAndReserveComponent,
        ReviewComponent,
        AddShiftFormComponent,
        CancelDialogComponent,
        ReviewComponent,
        ReasonDialogComponent,
        BusinessViewComponent,
        CardsUiComponent,
        BusinessRegisterComponent,
        ServicesComponent,
        SyncCalendarsComponent,
        FindBusinessDialogComponent,
        ErrorDialogComponent,
        ErrorDialogComponent,
        DetailsComponent,
        GoogleMapsComponent
    ],
    imports: [
        NgxSpinnerModule,
        CovalentFileModule,
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        BrowserAnimationsModule,
        MaterialModule,
        FlexLayoutModule,
        HttpClientModule,
        ReactiveFormsModule,
        AgmCoreModule.forRoot({
            apiKey: "AIzaSyBstSo5jhmmQQ5u2ZjEXOLbMIzXJIdV_48"
          })
        
    ],
    entryComponents: [
        ErrorDialogComponent,
        FindBusinessDialogComponent,
        TimePickerDialogComponent,
        CancelDialogComponent,
        ReasonDialogComponent,
        AssignServicesDialogComponent,
        ChangeClientToEmployeeDialogComponent
    ],
    providers: [
        AuthService,
        AuthGuard,
        AnonymousGuard,
        AdminGuard,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: UrlInterceptor,
            multi: true
        }
    ],
    
    bootstrap: [AppComponent]
})
export class AppModule {
}
