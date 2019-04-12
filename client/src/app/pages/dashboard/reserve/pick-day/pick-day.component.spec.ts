import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PickDayComponent } from './pick-day.component';

describe('PickDayComponent', () => {
  let component: PickDayComponent;
  let fixture: ComponentFixture<PickDayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PickDayComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PickDayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
