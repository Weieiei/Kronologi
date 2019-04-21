import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GuestAppointmentComponent } from './guest-appointment.component';

describe('GuestAppointmentComponent', () => {
  let component: GuestAppointmentComponent;
  let fixture: ComponentFixture<GuestAppointmentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GuestAppointmentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GuestAppointmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
