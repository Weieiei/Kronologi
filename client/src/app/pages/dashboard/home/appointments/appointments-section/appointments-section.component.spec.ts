import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentsSectionComponent } from './appointments-section.component';

describe('AppointmentsSectionComponent', () => {
  let component: AppointmentsSectionComponent;
  let fixture: ComponentFixture<AppointmentsSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppointmentsSectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppointmentsSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
