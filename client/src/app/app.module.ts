import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NavbarComponent } from './components/navbar/navbar.component';
import { RegisterComponent } from './pages/register/register.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthService } from './services/auth/auth.service';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { AnonymousGuard } from './guards/anonymous.guard';
import { UrlInterceptor } from './interceptor';
import { ReserveComponent } from './pages/reserve/reserve.component';
import { AppointmentsComponent } from './pages/appointments/appointments.component';
import { AdminGuard } from './guards/admin.guard';
import { SchedulerComponent } from './pages/reserve/scheduler/scheduler.component';
import { CalendarComponent } from './pages/reserve/scheduler/calendar/calendar.component';
import { MonthPickerComponent } from './pages/reserve/scheduler/month-picker/month-picker.component';
import { CustomStepperComponent } from './components/custom-stepper/custom-stepper.component';
import { ShiftPickerComponent } from './components/shift-picker/shift-picker.component';
import { TimePickerDialogComponent } from './components/shift-picker/time-picker-dialog/time-picker-dialog.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        RegisterComponent,
        HomeComponent,
        AppointmentsComponent,
        LoginComponent,
        ReserveComponent,
        AppointmentsComponent,
        SchedulerComponent,
        CalendarComponent,
        MonthPickerComponent,
        CustomStepperComponent,
        ShiftPickerComponent,
        TimePickerDialogComponent,
        DashboardComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        BrowserAnimationsModule,
        MaterialModule,
        FlexLayoutModule,
        HttpClientModule
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
