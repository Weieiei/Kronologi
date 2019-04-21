import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TermsAndServicesDialogComponent } from './terms-and-services-dialog.component';

describe('TermsAndServicesDialogComponent', () => {
  let component: TermsAndServicesDialogComponent;
  let fixture: ComponentFixture<TermsAndServicesDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TermsAndServicesDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TermsAndServicesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
