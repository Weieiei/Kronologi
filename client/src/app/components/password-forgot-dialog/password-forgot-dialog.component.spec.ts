import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordForgotDialogComponent } from './password-forgot-dialog.component';

describe('ErrorDialogComponent', () => {
  let component: PasswordForgotDialogComponent;
  let fixture: ComponentFixture<PasswordForgotDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PasswordForgotDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordForgotDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
