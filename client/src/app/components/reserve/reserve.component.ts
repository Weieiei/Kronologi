import { Component, OnInit, ViewChild } from '@angular/core';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { ServiceService } from 'src/app/services/service/service.service';
import * as moment from 'moment';
import { Appointment } from 'src/app/models/appointment/appointment';
import { Router } from '@angular/router';
import { CustomStepperComponent } from '../custom-stepper/custom-stepper.component';
import { Service } from '../../models/service/Service';

@Component({
    selector: 'app-reserve',
    templateUrl: './reserve.component.html',
    styleUrls: ['./reserve.component.scss']
})
export class ReserveComponent implements OnInit {

    @ViewChild('stepper') stepper: CustomStepperComponent;

    services: Service[] = [];

    appointment: Appointment;
    date: Date;
    startTime: string;
    endTime: string;

    constructor(
        private appointmentService: AppointmentService,
        private serviceService: ServiceService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.appointment = new Appointment();
        this.getServices();
    }

    getServices() {
        this.serviceService.getServices().subscribe(
            res => this.services = res,
            err => console.log(err)
        );
    }

    private findServiceById(id: number): Service {
        return this.services.find(service => service.getId() === id);
    }

    updateEndTime() {
        if (this.appointment.service_id !== undefined && this.startTime !== undefined) {
            const service: Service = this.findServiceById(this.appointment.service_id);
            const date = moment('2012-12-12 ' + this.startTime).add(service.getDuration(), 'm');
            this.endTime = date.format('HH:mm:ss');
        }
    }

    makeAppointment(): void {
        const date = moment(this.date).format('YYYY-MM-DD');
        this.appointment.start_time = moment(date + ' ' + this.startTime).format('YYYY-MM-DD HH:mm:ss');
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
