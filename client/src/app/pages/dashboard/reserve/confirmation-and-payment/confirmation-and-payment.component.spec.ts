import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmationAndPaymentComponent } from './confirmation-and-payment.component';

describe('ConfirmationAndPaymentComponent', () => {
  let component: ConfirmationAndPaymentComponent;
  let fixture: ComponentFixture<ConfirmationAndPaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmationAndPaymentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmationAndPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
