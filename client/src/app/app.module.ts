import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

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
import { EmployeeAppointmentsComponent } from './pages/dashboard/home/employee-appointments/employee-appointments.component';
import { AutofocusDirective } from './directives/autofocus/autofocus.directive';
import { SettingsComponent } from './pages/settings/settings.component';
import { AccountSettingsComponent } from './pages/settings/account-settings/account-settings.component';
import { ReminderSettingsComponent } from './pages/settings/reminder-settings/reminder-settings.component';
import { EmployeePickerComponent } from './pages/dashboard/reserve/employee-picker/employee-picker.component';
import { ServiceSelectionComponent } from './pages/dashboard/reserve/service-selection/service-selection.component';
import { TimePickerComponent } from './pages/dashboard/reserve/time-picker/time-picker.component';
import { NotesAndReserveComponent } from './pages/dashboard/reserve/notes-and-reserve/notes-and-reserve.component';
import { ReviewComponent } from './pages/dashboard/review/review.component';

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
        CardSizeDirective,
        EmployeeAppointmentsComponent,
        AutofocusDirective,
        SettingsComponent,
        AccountSettingsComponent,
        ReminderSettingsComponent,
        EmployeePickerComponent,
        ServiceSelectionComponent,
        TimePickerComponent,
        NotesAndReserveComponent,
        ReviewComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        BrowserAnimationsModule,
        MaterialModule,
        FlexLayoutModule,
        HttpClientModule,
        ReactiveFormsModule
    ],
    entryComponents: [
        TimePickerDialogComponent
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
