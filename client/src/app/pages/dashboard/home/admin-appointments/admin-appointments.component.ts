import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../../../services/appointment/appointment.service';
import { AppointmentDetailed } from '../../../../models/appointment/AppointmentDetailed';
import { map } from 'rxjs/operators';
import { User } from '../../../../models/user/User';
import { Service } from '../../../../models/service/Service';
import { ThemeService } from '../../../../core/theme/theme.service';
import { AdminEmployeeDTO } from '../../../../interfaces/employee/admin-employee-dto';
import { UserToDisplay } from '../../../../models/user/UserToDisplay';
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-admin-appointments',
    templateUrl: './admin-appointments.component.html',
    styleUrls: ['./admin-appointments.component.scss']
})
export class AdminAppointmentsComponent implements OnInit {

    businessId : number;
    displayedColumns: string[] = ['service', 'date', 'time', 'duration', 'client', 'employee'];
    appointments: AppointmentDetailed[];
    darkModeActive: boolean;

    componentState: {
        appointments: Array<AppointmentDetailed>,
        // currentSort: IDataTableSort,
        currentPage: number,
        itemsPerPage: number,
        search: string,
        totalItems: number,
    };

    constructor(private route : ActivatedRoute, private appointmentService: AppointmentService, private themeService: ThemeService) {

    }

    ngOnInit() {
        this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"));

        this.componentState = {
            appointments: [],
            // currentSort: IDataTableSort,
            currentPage: 1,
            itemsPerPage: 10,
            search: '',
            totalItems: 0,
        };

        this.getAllAppointments(this.businessId);
        this.themeService.darkModeState.subscribe( value => {
            this.darkModeActive = value;
        });
    }

    getAllAppointments(businessId : number): void {
        this.appointmentService.getAllAppointments(businessId).pipe(
            map(data => {
                return data.map(a => {
                    const client = a.client;
                    const employee = a.employee;
                    const service = a.service;

                    return new AppointmentDetailed(
                        a.id, a.clientId, a.employeeId, a.serviceId,
                        a.startTime, a.endTime, a.date, a.notes,
                        a.status, a.createdAt, a.updatedAt,
                        new User(
                            client.id, client.firstName, client.lastName,
                            client.email, client.username, client.password,
                            client.userType, client.createdAt, client.updatedAt
                        ),
                        new User(
                            employee.id, employee.firstName, employee.lastName,
                            employee.email, employee.username, employee.password,
                            employee.userType, employee.createdAt, employee.updatedAt
                        ),
                        new Service(
                            service.id, service.name, service.duration,
                        )
                    );

                });

            })
        ).subscribe(
            res => {
                this.appointments = res;
                this.componentState.appointments = res;
                this.updateAllAppointments(this.appointments);
            },
            err => console.log(err)
        );
    }

    filterItems(items: Array<AppointmentDetailed>, search: string): Array<AppointmentDetailed> {
        if (search.length < 1) {
            return items;
        }

        const searchCriteria = search.toLowerCase();
        return items.filter((item) => item.service.name.toLowerCase().includes(searchCriteria)
            || item.client.firstName.toLowerCase().includes(searchCriteria)
            || item.client.lastName.toLowerCase().includes(searchCriteria)
            || item.employee.firstName.toLowerCase().includes(searchCriteria)
            || item.employee.lastName.toLowerCase().includes(searchCriteria));
    }

    updateAllAppointments(allItems: Array<AppointmentDetailed>): void {
        if (allItems) {
            const filteredItems = this.filterItems(allItems, this.componentState.search);
            const totalItems = filteredItems.length;
            this.componentState.appointments = filteredItems;
            this.componentState.totalItems = totalItems;
        }
    }


    onSearch(searchTerm: string) {
        this.componentState.currentPage = 1;
        this.componentState.search = searchTerm;
        this.updateAllAppointments(this.appointments);
    }


}
