import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRecurringShiftFormComponent } from './add-recurring-shift-form.component';

describe('AddRecurringShiftFormComponent', () => {
  let component: AddRecurringShiftFormComponent;
  let fixture: ComponentFixture<AddRecurringShiftFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddRecurringShiftFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRecurringShiftFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
