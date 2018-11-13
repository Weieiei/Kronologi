import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyAppointmentsComponent } from './my-appointments.component';
import { Component, NO_ERRORS_SCHEMA, DebugElement } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { MaterialModule } from '../../material';
import {TranslateFakeLoader, TranslateLoader, TranslateModule} from "@ngx-translate/core";

@Component({ selector: 'app-appointment', template: '' })
class AppointmentStubComponent { }

describe('MyAppointmentsComponent', () => {
  let component: MyAppointmentsComponent;
  let fixture: ComponentFixture<MyAppointmentsComponent>;

  let table: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        RouterTestingModule,
        MaterialModule,
        FormsModule,
        TranslateModule.forRoot({
          loader: { provide: TranslateLoader, useClass: TranslateFakeLoader },
        }),
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

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display n appointments if appointments array has n items', () => {

    component.appointments = [
      { name: 'service', start_time: '2018-11-14 19:00', end_time: '2018-11-14 21:00', duration: 45, notes: 'notes' },
      { name: 'service', start_time: '2018-11-14 19:00', end_time: '2018-11-14 21:00', duration: 45, notes: 'notes' },
      { name: 'service', start_time: '2018-11-14 19:00', end_time: '2018-11-14 21:00', duration: 45, notes: 'notes' }
    ];

    fixture.detectChanges();

    table = fixture.debugElement.query(By.css('table'));
    // count the number of tags with attribute class="mat-row"
    // the header is mat-header-row so it won't show up
    expect((table.nativeElement.innerHTML.match(/class="mat-row"/g) || []).length).toEqual(component.appointments.length);

  });
});