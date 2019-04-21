import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { StarRatingModule } from 'angular-star-rating';
import { LightboxModule } from 'ngx-lightbox';
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
import { AppointmentsComponent } from './pages/dashboard/home/appointments/appointments.component';
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
import { AdminAppointmentsComponent } from './pages/dashboard/home/admin-appointments/admin-appointments.component';
import { VerifiedComponent } from './pages/verified/verified.component';
import { EmployeeAppointmentsComponent } from './pages/dashboard/home/employee-appointments/employee-appointments.component';
import { AutofocusDirective } from './directives/autofocus/autofocus.directive';
import { SettingsComponent } from './pages/settings/settings.component';
import { AccountSettingsComponent } from './pages/settings/account-settings/account-settings.component';
import { ReminderSettingsComponent } from './pages/settings/reminder-settings/reminder-settings.component';
import { ShiftComponent } from './pages/dashboard/home/shift/shift.component';
import { CancelDialogComponent } from './components/cancel-dialog/cancel-dialog.component';
import { NotesAndReserveComponent } from './pages/dashboard/reserve/notes-and-reserve/notes-and-reserve.component';
import { ReviewComponent } from './pages/dashboard/review/review.component';
import { AdminEmployeesComponent } from './pages/dashboard/home/admin-employees/admin-employees.component';
import { AddShiftFormComponent } from './pages/dashboard/home/shift/add-shift-form/add-shift-form.component';
import { AdminServicesComponent } from './pages/dashboard/home/admin-services/admin-services.component';
import { CreateServiceComponent } from './pages/create-service/create-service.component';
import { AdminUsersComponent } from './pages/dashboard/home/admin-users/admin-users.component';
import { AssignServicesDialogComponent } from './pages/dashboard/home/admin-users/assign-services-dialog/assign-services-dialog.component';
import { ChangeClientToEmployeeDialogComponent } from './pages/dashboard/home/admin-users/change-client-to-employee-dialog/change-client-to-employee-dialog.component';
import { ReasonDialogComponent } from './components/reason-dialog/reason-dialog.component';
import { BusinessViewComponent } from './pages/business-view/business-view.component';
import { CardsUiComponent } from './components/cards-ui/cards-ui.component';
import { BusinessRegisterComponent } from './pages/register/business-register/business-register.component';
import { NgxSpinnerModule } from 'ngx-spinner';
import { SyncCalendarsComponent } from './pages/sync-calendars/sync-calendars.component';
import { FindBusinessDialogComponent } from './components/find-business-dialog/find-business-dialog.component';
import { AgmCoreModule } from '@agm/core';
import { ErrorDialogComponent } from './components/error-dialog/error-dialog.component';
import { DetailsComponent } from './pages/details/details.component';
import { GoogleMapsComponent } from './components/google-maps/google-maps.component';
import { AddRecurringShiftFormComponent } from './pages/dashboard/home/shift/add-recurring-shift-form/add-recurring-shift-form.component';
import { SwiperModule } from 'angular2-useful-swiper';
import { SlickCarouselModule } from 'ngx-slick-carousel';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { ServiceSelectionGridListComponent } from './pages/dashboard/reserve/service-selection-grid-list/service-selection-grid-list.component';
import { BookComponent } from './pages/dashboard/reserve/book/book.component';
import { PickDayComponent } from './pages/dashboard/reserve/pick-day/pick-day.component';
import { SearchInputComponent } from './components/search-box/search-box.component';
import { PasswordResetRedirectComponent } from './pages/password-reset-redirect/password-reset-redirect.component';
import { PasswordForgotDialogComponent } from './components/password-forgot-dialog/password-forgot-dialog.component';


@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        RegisterComponent,
        HomeComponent,
        LoginComponent,
        AppointmentsComponent,
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
        NotesAndReserveComponent,
        ReviewComponent,
        AddShiftFormComponent,
        CancelDialogComponent,
        ReviewComponent,
        ReasonDialogComponent,
        BusinessViewComponent,
        CardsUiComponent,
        BusinessRegisterComponent,
        SyncCalendarsComponent,
        AddRecurringShiftFormComponent,
        SearchInputComponent,
        ServiceSelectionGridListComponent,
        BookComponent,
        PickDayComponent,
        SearchInputComponent,
        SyncCalendarsComponent,
        FindBusinessDialogComponent,
        ErrorDialogComponent,
        ErrorDialogComponent,
        DetailsComponent,
        GoogleMapsComponent,
        PasswordResetRedirectComponent,
        PasswordForgotDialogComponent
    ],
    imports: [
        LightboxModule,
        CovalentFileModule,
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
        SwiperModule,
        SlickCarouselModule,
        MatTabsModule,
        MatDatepickerModule,
        ReactiveFormsModule,
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyBstSo5jhmmQQ5u2ZjEXOLbMIzXJIdV_48'
          }),
        StarRatingModule.forRoot()
    ],
    entryComponents: [
        ErrorDialogComponent,
        FindBusinessDialogComponent,
        TimePickerDialogComponent,
        CancelDialogComponent,
        ReasonDialogComponent,
        AssignServicesDialogComponent,
        ChangeClientToEmployeeDialogComponent,
        PasswordForgotDialogComponent
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
