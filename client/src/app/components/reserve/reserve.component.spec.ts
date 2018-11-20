import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReserveComponent } from './reserve.component';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/material';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SchedulerComponent } from '../scheduler/scheduler.component';
import { SlidePanelComponent } from '../scheduler/slide-panel/slide-panel.component';
import { CalendarComponent } from '../scheduler/calendar/calendar.component';
import { MonthPickerComponent } from '../scheduler/month-picker/month-picker.component';

describe('ReserveComponent', () => {
  let component: ReserveComponent;
  let fixture: ComponentFixture<ReserveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        MaterialModule,
        HttpClientModule,
        RouterTestingModule,
        BrowserAnimationsModule
      ],
      declarations: [
        ReserveComponent,
        SchedulerComponent,
        CalendarComponent,
        MonthPickerComponent,
        SlidePanelComponent
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReserveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
