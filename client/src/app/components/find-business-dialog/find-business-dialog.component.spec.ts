import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FindBusinessDialogComponent } from './find-business-dialog.component';

describe('FindBusinessDialogComponent', () => {
  let component: FindBusinessDialogComponent;
  let fixture: ComponentFixture<FindBusinessDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FindBusinessDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FindBusinessDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
