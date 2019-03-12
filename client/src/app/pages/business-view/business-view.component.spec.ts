import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BusinessViewComponent } from './business-view.component';

describe('BusinessViewComponent', () => {
  let component: BusinessViewComponent;
  let fixture: ComponentFixture<BusinessViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BusinessViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BusinessViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
