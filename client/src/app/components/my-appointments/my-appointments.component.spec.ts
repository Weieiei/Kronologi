import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyAppointmentsComponent } from './my-appointments.component';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { MaterialModule } from 'src/app/material';
import { FormsModule } from '@angular/forms';

@Component({ selector: 'app-appointment', template: '' })
class AppointmentStubComponent { }

fdescribe('MyAppointmentsComponent', () => {
  let component: MyAppointmentsComponent;
  let fixture: ComponentFixture<MyAppointmentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        RouterTestingModule,
        MaterialModule,
        FormsModule
      ],
      declarations: [
        MyAppointmentsComponent,
        AppointmentStubComponent
      ],
      schemas: [
        NO_ERRORS_SCHEMA
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyAppointmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should not create if user is not logged in', () => {
    expect(component).toBeTruthy();
  });
});
