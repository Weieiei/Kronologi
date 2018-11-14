import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentsComponent } from './appointments.component';
import { HttpClientModule } from "@angular/common/http";
import { RouterTestingModule } from "@angular/router/testing";
import { MaterialModule } from "../../material";
import { By } from "@angular/platform-browser";
import { DebugElement } from "@angular/core";

describe('AppointmentsComponent', () => {
  let component: AppointmentsComponent;
  let fixture: ComponentFixture<AppointmentsComponent>;

  let table: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        RouterTestingModule,
        MaterialModule
      ],
      declarations: [ AppointmentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppointmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display n appointments if dataSource array has n items', () => {

    component.dataSource = [
      {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
      {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
      {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
      {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
    ];

    fixture.detectChanges();

    table = fixture.debugElement.query(By.css('table'));
    // count the number of tags with attribute class="mat-row"
    // the header is mat-header-row so it won't show up
    expect((table.nativeElement.innerHTML.match(/class="mat-row"/g) || []).length).toEqual(component.dataSource.length);

  });
});
