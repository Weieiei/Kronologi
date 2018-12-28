import { Component, OnInit, ViewChild } from '@angular/core';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { ServiceService } from 'src/app/services/service/service.service';
import * as moment from 'moment';
import { Router } from '@angular/router';
import { CustomStepperComponent } from '../custom-stepper/custom-stepper.component';
import { Service } from '../../models/service/Service';
import { AppointmentToBook } from '../../models/appointment/AppointmentToBook';
import { map } from 'rxjs/operators';

@Component({
    selector: 'app-reserve',
    templateUrl: './reserve.component.html',
    styleUrls: ['./reserve.component.scss']
})
export class ReserveComponent implements OnInit {

    @ViewChild('stepper') stepper: CustomStepperComponent;

    services: Service[] = [];

    appointment: AppointmentToBook;
    date: Date;
    endTime: string;

    employeeId: number;
    serviceId: number;
    startTime: Date;
    notes: string;

    constructor(
        private appointmentService: AppointmentService,
        private serviceService: ServiceService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.getServices();
    }

    getServices() {
        this.serviceService.getServices().pipe(
            map(data => {
                this.services = data.map(s => new Service(s.id, s.name, s.duration, s.createdAt, s.updatedAt));
            })
        ).subscribe(
            res => void 0,
            err => console.log(err)
        );
    }

    private findServiceById(id: number): Service {
        return this.services.find(service => service.id === id);
    }

    updateEndTime() {
        if (this.appointment.serviceId !== undefined && this.startTime !== undefined) {
            const service: Service = this.findServiceById(this.appointment.serviceId);
            const date = moment('2012-12-12 ' + this.startTime).add(service.duration, 'm');
            this.endTime = date.format('HH:mm:ss');
        }
    }

    makeAppointment(): void {
        const date = moment(this.date).format('YYYY-MM-DD');
        this.appointment.startTime = new Date(moment(date + ' ' + this.startTime).format('YYYY-MM-DD HH:mm:ss'));
        this.appointmentService.reserveAppointment(this.appointment).subscribe(
            res => this.router.navigate(['/my/appts']),
            err => console.log(err)
        );
    }

    setDate(date: Date): void {
        this.date = date;
        this.stepper.next();
    }

}
