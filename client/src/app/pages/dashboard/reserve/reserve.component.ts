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
import { Subject } from 'rxjs';
import { UserAppointmentDTO } from '../../../interfaces/appointment/user-appointment-dto';

@Component({
    selector: 'app-reserve',
    templateUrl: './reserve.component.html',
    styleUrls: ['./reserve.component.scss']
})
export class ReserveComponent implements OnInit {

    @ViewChild('stepper') stepper: CustomStepperComponent;

    service: ServiceDTO;

    appointment: UserAppointmentDTO;
    date: Date;
    endTime: string;

    employeeId: number;
    serviceId: number;
    notes: string;

    modifyAppointment: boolean;

    employees: EmployeeDTO[];
    employee: EmployeeDTO;
    employeeShift: ShiftDTO;
    employeeAppointments: EmployeeAppointmentDTO[];

    startTime: string;

    serviceSubject = new Subject<number>();
    dateSubject = new Subject<string>();
    employeeSubject = new Subject<number>();
    startTimeSubject = new Subject<string>();
    notesSubject = new Subject<string>();

    appointmentDate: string;
    payload: BookAppointmentDTO;

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
                            setTimeout(() => this.notifyAll());
                        }
                    }
                );

            }

        });
    }

    notifyAll(): void {
        this.serviceSubject.next(this.appointment.service.id);
        this.dateSubject.next(this.appointment.date);
        this.employeeSubject.next(this.appointment.employee.id);
        this.startTimeSubject.next(this.appointment.startTime);
        this.notesSubject.next(this.appointment.notes);
    }

    notifyService(): void {
        if (this.appointment) {
            this.serviceSubject.next(this.appointment.service.id);
        }
    }

    notifyDate(): void {
        if (this.appointment) {
            this.dateSubject.next(this.appointment.date);
        }
    }

    notifyEmployee(): void {
        if (this.appointment) {
            this.employeeSubject.next(this.appointment.employee.id);
        }
    }

    notifyStartTime(): void {
        if (this.appointment) {
            this.startTimeSubject.next(this.appointment.startTime);
        }
    }

    notifyNotes(): void {
        if (this.appointment) {
            this.notesSubject.next(this.appointment.notes);
        }
    }

    setService(service: ServiceDTO): void {
        this.service = service;
        this.stepper.next();
        this.notifyService();
    }

    setDate(date: Date): void {
        this.date = date;
        this.stepper.next();
        this.getAvailableEmployeesByServiceAndByDate();
        this.notifyDate();
    }

    setEmployee(employee: EmployeeDTO): void {
        this.employee = employee;
        this.stepper.next();
        this.getSelectedEmployeesShiftByDate();
        this.getSelectedEmployeesAppointmentsByDate();
        this.notifyEmployee();
    }

    setTime(time: string): void {
        this.startTime = time;
        this.endTime = moment(this.startTime, 'HH:mm').add(this.service.duration, 'm').format('HH:mm');
        this.stepper.next();
        this.notifyStartTime();
    }

    setNotesAndReserve(notes: string) {
        this.notes = notes;
        this.reserve();
    }

    getAppointmentById(id: number) {
        this.appointmentService.getAppointmentById(id).subscribe(
            res => {
                this.appointment = res;
                this.notifyAll();
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

        this.appointmentDate = `${year}-${month < 10 ? '0' + month : month}-${day < 10 ? '0' + day : day}`;

        this.payload = {
            employeeId: this.employee.id,
            serviceId: this.service.id,
            date: this.appointmentDate,
            startTime: this.startTime,
            notes: this.notes
        };

        if (this.modifyAppointment) {
            this.updateAppointment();
        } else {
            this.bookAppointment();
        }

    }

    bookAppointment() {

        this.appointmentService.bookAppointment(this.payload).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess('You\'ve successfully booked your appointment!', 5000);
                this.router.navigate(['']);
            },
            err => {
                if (err instanceof HttpErrorResponse) {
                    this.snackBar.openSnackBarError(err.error.message);
                }
            }
        );

    }

    updateAppointment() {

        this.appointmentService.updateAppointment(this.appointment.id, this.payload).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess('You\'ve successfully modified your appointment!', 5000);
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
