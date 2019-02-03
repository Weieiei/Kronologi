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
import { ShiftDTO } from '../../../interfaces/shift-dto/shift-dto';
import { EmployeeAppointmentDTO } from '../../../interfaces/appointment/employee-appointment-dto';
import { ServiceDTO } from '../../../interfaces/service/service-dto';
import { BookAppointmentDTO } from '../../../interfaces/appointment/book-appointment-dto';
import * as moment from 'moment';

@Component({
    selector: 'app-reserve',
    templateUrl: './reserve.component.html',
    styleUrls: ['./reserve.component.scss']
})
export class ReserveComponent implements OnInit {

    @ViewChild('stepper') stepper: CustomStepperComponent;

    service: ServiceDTO;

    appointment;
    date: Date;
    endTime: string;

    employeeId: number;
    serviceId: number;
    notes: string;

    modifyAppointment: boolean;
    isLoaded: boolean;

    employees: EmployeeDTO[];
    employee: EmployeeDTO;
    employeeShift: ShiftDTO;
    employeeAppointments: EmployeeAppointmentDTO[];

    startTime: string;

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

    setService(service: ServiceDTO): void {
        this.service = service;
        this.stepper.next();
    }

    setDate(date: Date): void {
        this.date = date;
        this.stepper.next();
        this.getAvailableEmployeesByServiceAndByDate();
    }

    setEmployee(employee: EmployeeDTO): void {
        this.employee = employee;
        this.stepper.next();
        this.getSelectedEmployeesShiftByDate();
        this.getSelectedEmployeesAppointmentsByDate();
    }

    setTime(time: string): void {
        this.startTime = time;
        this.endTime = moment(this.startTime, 'HH:mm').add(this.service.duration, 'm').format('HH:mm');
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

    getAvailableEmployeesByServiceAndByDate() {
        this.employeeService.getAvailableEmployeesByServiceAndByDate(this.service.id, this.date.toLocaleDateString()).subscribe(
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

    reserve() {

        const year = this.date.getFullYear();
        const month = this.date.getMonth() + 1;
        const day = this.date.getDate();

        const appointmentDate = `${year}-${month < 10 ? '0' + month : month}-${day}`;

        const payload: BookAppointmentDTO = {
            employeeId: this.employee.id,
            serviceId: this.service.id,
            date: appointmentDate,
            startTime: this.startTime,
            notes: this.notes
        };

        this.appointmentService.bookAppointment(payload).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess('Successfully booked an appointment!');
                this.router.navigate(['']);
            },
            err => {
                if (err instanceof HttpErrorResponse) {
                    this.snackBar.openSnackBarError(err.error.message);
                }
            }
        );

    }

}
