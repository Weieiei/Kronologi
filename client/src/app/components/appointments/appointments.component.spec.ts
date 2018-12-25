import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentsComponent } from './appointments.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { MaterialModule } from '../../material';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { Service } from '../../models/service/Service';
import { User } from '../../models/user/User';
import { UserType } from '../../models/user/UserType';
import { AppointmentDetailed } from '../../models/appointment/AppointmentDetailed';

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
            declarations: [AppointmentsComponent]
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

        const client = new User(1, 'John', 'Doe', 'john@doe.com', 'johndoe', '', UserType.client, new Date(), null);
        const employee = new User(2, 'Test', 'Employee', 'test@test.com', 'testing', '', UserType.employee, new Date(), null);
        const service = new Service(1, 'Some service', 120);

        const appointment1 = new AppointmentDetailed(1, 1, 2, 1, new Date(), new Date(), '', new Date(), null, client, employee, service);
        const appointment2 = new AppointmentDetailed(2, 1, 2, 1, new Date(), new Date(), '', new Date(), null, client, employee, service);

        component.appointments = [appointment1, appointment2];

        fixture.detectChanges();

        table = fixture.debugElement.query(By.css('table'));
        // count the number of tags with attribute class="mat-row"
        // the header is mat-header-row so it won't show up
        expect((table.nativeElement.innerHTML.match(/class="mat-row"/g) || []).length).toEqual(component.appointments.length);

    });
});
