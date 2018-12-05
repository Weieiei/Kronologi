import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material';
import { FlexModule } from '@angular/flex-layout';
import { NavbarComponent } from './components/navbar/navbar.component';
import { RegisterComponent } from './components/register/register.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthService } from './services/auth/auth.service';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { AnonymousGuard } from './guards/anonymous.guard';
import { UrlInterceptor } from './interceptor';
import { AppointmentsComponent } from './components/appointments/appointments.component';
import { ReserveComponent } from './components/reserve/reserve.component';
import { MyAppointmentsComponent } from './components/my-appointments/my-appointments.component';
import { SchedulerComponent } from './components/scheduler/scheduler.component';
import { SlidePanelComponent } from './components/scheduler/slide-panel/slide-panel.component';
import { CalendarComponent } from './components/scheduler/calendar/calendar.component';
import { MonthPickerComponent } from './components/scheduler/month-picker/month-picker.component';
import { CustomStepperComponent } from './components/custom-stepper/custom-stepper.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    RegisterComponent,
    HomeComponent,
    AppointmentsComponent,
    LoginComponent,
    ReserveComponent,
    MyAppointmentsComponent,
    SchedulerComponent,
    SlidePanelComponent,
    CalendarComponent,
    MonthPickerComponent,
    CustomStepperComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    MaterialModule,
    FlexModule,
    HttpClientModule,
    BrowserModule
  ],
  providers: [
    AuthService,
    AuthGuard,
    AnonymousGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: UrlInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
