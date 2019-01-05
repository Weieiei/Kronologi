import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthPickerComponent } from './month-picker.component';
import { MaterialModule } from '../../../../material';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('MonthPickerComponent', () => {
  let component: MonthPickerComponent;
  let fixture: ComponentFixture<MonthPickerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MaterialModule,
        FormsModule,
        BrowserAnimationsModule
      ],
      declarations: [ MonthPickerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonthPickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
