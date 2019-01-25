import { Component, OnInit, ViewChild } from '@angular/core';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { ServiceService } from 'src/app/services/service/service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomStepperComponent } from '../../../components/custom-stepper/custom-stepper.component';
import { map } from 'rxjs/operators';

@Component({
    selector: 'app-reserve',
    templateUrl: './reserve.component.html',
    styleUrls: ['./reserve.component.scss']
})
export class ReserveComponent implements OnInit {

    @ViewChild('stepper') stepper: CustomStepperComponent;

    service: any;

    appointment;
    date: Date;
    endTime: string;

    employeeId: number;
    serviceId: number;
    startTime: Date;
    notes: string;

    loaded: boolean;

    constructor(
        private appointmentService: AppointmentService,
        private serviceService: ServiceService,
        private router: Router,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.route.paramMap.pipe(
            map(() => window.history.state.appointment)
        ).subscribe(
            res => {
                this.appointment = res;
                this.loaded = true;
            }
        );
    }

    /*getServices() {
        this.serviceService.getServices().subscribe(
            res => this.services = res,
            err => console.log(err)
        );
    }

    private findServiceById(id: number): Service {
        return this.services.find(service => service.getId() === id);
    }

    updateEndTime() {
        if (this.appointment.getServiceId() !== undefined && this.startTime !== undefined) {
            const service: Service = this.findServiceById(this.appointment.getServiceId());
            const date = moment('2012-12-12 ' + this.startTime).add(service.getDuration(), 'm');
            this.endTime = date.format('HH:mm:ss');
        }
    }

    makeAppointment(): void {
        const date = moment(this.date).format('YYYY-MM-DD');
        this.appointment.setStartTime(new Date(moment(date + ' ' + this.startTime).format('YYYY-MM-DD HH:mm:ss')));
        this.appointmentService.reserveAppointment(this.appointment).subscribe(
            res => this.router.navigate(['/my/appts']),
            err => console.log(err)
        );
    }*/

    setDate(date: Date): void {
        this.date = date;
        this.stepper.next();
    }

    setService(service: any): void {
        this.service = service;
        this.stepper.next();
    }

}
