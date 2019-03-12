import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SyncCalendarsComponent } from './sync-calendars.component';

describe('SyncCalendarsComponent', () => {
  let component: SyncCalendarsComponent;
  let fixture: ComponentFixture<SyncCalendarsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SyncCalendarsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SyncCalendarsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
