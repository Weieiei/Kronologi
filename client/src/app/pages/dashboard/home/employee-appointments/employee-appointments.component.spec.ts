import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeAppointmentsComponent } from './employee-appointments.component';

describe('EmployeeAppointmentsComponent', () => {
  let component: EmployeeAppointmentsComponent;
  let fixture: ComponentFixture<EmployeeAppointmentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeeAppointmentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeAppointmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
