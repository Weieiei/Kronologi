import { Component, OnInit, ViewChild } from '@angular/core';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { ServiceService } from 'src/app/services/service/service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomStepperComponent } from '../../../components/custom-stepper/custom-stepper.component';
import { map } from 'rxjs/operators';
import { SnackBar } from '../../../snackbar';
import { HttpErrorResponse } from '@angular/common/http';
import { EmployeeDTO } from '../../../interfaces/user/employee-dto';
import { EmployeeService } from '../../../services/employee/employee.service';

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

    modifyAppointment: boolean;
    isLoaded: boolean;

    employees: EmployeeDTO[];
    employee: EmployeeDTO;
    employeeShift: any;
    employeeAppointments: any;

    time: string;

    constructor(
        private appointmentService: AppointmentService,
        private serviceService: ServiceService,
        private employeeService: EmployeeService,
        private router: Router,
        private route: ActivatedRoute,
        private snackBar: SnackBar
    ) {
    }

    ngOnInit() {
        this.route.data.subscribe(data => {

            this.modifyAppointment = data.edit;

            if (this.modifyAppointment) {

                this.route.paramMap.pipe(
                    map(() => window.history.state.appointment)
                ).subscribe(
                    res => {
                        this.appointment = res;
                        if (!this.appointment) {
                            const id = +this.route.snapshot.paramMap.get('id');

                            if (isNaN(id)) {
                                this.snackBar.openSnackBarError('Invalid URL.');
                                this.router.navigate(['']);
                                return;
                            }

                            this.getAppointmentById(id);
                        } else {
                            this.isLoaded = true;
                        }
                    }
                );

            } else {
                this.isLoaded = true;
            }

        });
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
        this.getAvailableEmployeesByDate();
    }

    setService(service: any): void {
        this.service = service;
        this.stepper.next();
    }

    setEmployee(employee: EmployeeDTO): void {
        this.employee = employee;
        this.stepper.next();
        this.getSelectedEmployeesShiftByDate();
        this.getSelectedEmployeesAppointmentsByDate();
    }

    setTime(time: string): void {
        // this.date = new Date(this.date.toLocaleDateString() + ' ' + time);
        this.time = time;
        this.stepper.next();
    }

    getAppointmentById(id: number) {
        this.appointmentService.getAppointmentById(id).subscribe(
            res => {
                this.appointment = res;
                this.isLoaded = true;
            },
            err => {
                if (err instanceof HttpErrorResponse) {
                    this.snackBar.openSnackBarError(err.error.message);
                    this.router.navigate(['']);
                }
            }
        );
    }

    getAvailableEmployeesByDate() {
        this.employeeService.getAvailableEmployeesByDate(this.date.toLocaleDateString()).subscribe(
            res => this.employees = res
        );
    }

    getSelectedEmployeesShiftByDate() {
        this.employeeService.getSelectedEmployeesShiftByDate(this.employee.id, this.date.toLocaleDateString()).subscribe(
            res => this.employeeShift = res
        );
    }

    getSelectedEmployeesAppointmentsByDate() {
        this.employeeService.getSelectedEmployeesAppointmentsByDate(this.employee.id, this.date.toLocaleDateString()).subscribe(
            res => this.employeeAppointments = res
        );
    }
}
