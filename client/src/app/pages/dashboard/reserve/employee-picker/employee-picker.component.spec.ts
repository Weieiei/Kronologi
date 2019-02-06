import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeePickerComponent } from './employee-picker.component';

describe('EmployeePickerComponent', () => {
  let component: EmployeePickerComponent;
  let fixture: ComponentFixture<EmployeePickerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeePickerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
