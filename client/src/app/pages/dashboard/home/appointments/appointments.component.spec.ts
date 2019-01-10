import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentsComponent } from './appointments.component';
import { Component, NO_ERRORS_SCHEMA, DebugElement } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { MaterialModule } from '../../../../material';
import { User } from '../../../../models/user/User';
import { UserType } from '../../../../models/user/UserType';
import { Service } from '../../../../models/service/Service';
import { MyAppointment } from '../../../../models/appointment/MyAppointment';
import { AppointmentStatus } from '../../../../models/appointment/AppointmentStatus';

@Component({selector: 'app-appointment', template: ''})
class AppointmentStubComponent {
}

describe('AppointmentsComponent', () => {
    let component: AppointmentsComponent;
    let fixture: ComponentFixture<AppointmentsComponent>;

    let table: DebugElement;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientModule,
                RouterTestingModule,
                MaterialModule,
                FormsModule
            ],
            declarations: [
                AppointmentsComponent,
                AppointmentStubComponent
            ],
            schemas: [
                NO_ERRORS_SCHEMA
            ]
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

    it('should display n appointments if appointments array has n items', () => {

        const employee = new User(2, 'Test', 'Employee', 'test@test.com', 'testing', '', UserType.employee, new Date(), null);
        const service = new Service(1, 'Some service', 120, new Date(), null);

        const appointment1 = new MyAppointment(1, 1, 2, 1, new Date(), new Date(), '', AppointmentStatus.confirmed, new Date(), null, employee, service);
        const appointment2 = new MyAppointment(2, 1, 2, 1, new Date(), new Date(), '', AppointmentStatus.confirmed, new Date(), null, employee, service);

        component.appointments = [appointment1, appointment2];

        fixture.detectChanges();

        table = fixture.debugElement.query(By.css('table'));
        // count the number of tags with attribute class="mat-row"
        // the header is mat-header-row so it won't show up
        expect((table.nativeElement.innerHTML.match(/class="mat-row"/g) || []).length).toEqual(component.appointments.length);

    });
});
