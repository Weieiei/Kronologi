import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoutingAndAccountHelpComponent } from './routing-and-account-help.component';

describe('RoutingAndAccountHelpComponent', () => {
  let component: RoutingAndAccountHelpComponent;
  let fixture: ComponentFixture<RoutingAndAccountHelpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoutingAndAccountHelpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoutingAndAccountHelpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
