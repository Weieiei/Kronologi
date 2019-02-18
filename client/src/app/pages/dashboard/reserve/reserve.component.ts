import { Component, OnInit, ViewChild } from '@angular/core';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { ServiceService } from 'src/app/services/service/service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomStepperComponent } from '../../../components/custom-stepper/custom-stepper.component';
import { map } from 'rxjs/operators';
import { SnackBar } from '../../../snackbar';
import { HttpErrorResponse } from '@angular/common/http';
import { EmployeeDTO } from '../../../interfaces/employee/employee-dto';
import { EmployeeService } from '../../../services/employee/employee.service';
import { ShiftDTO } from '../../../interfaces/shift/shift-dto';
import { EmployeeAppointmentDTO } from '../../../interfaces/appointment/employee-appointment-dto';
import { ServiceDTO } from '../../../interfaces/service/service-dto';
import { BookAppointmentDTO } from '../../../interfaces/appointment/book-appointment-dto';
import { Subject } from 'rxjs';
import { UserAppointmentDTO } from '../../../interfaces/appointment/user-appointment-dto';
import { HelperService } from '../../../services/helper/helper.service';
import { EmployeeTimes } from '../../../interfaces/employee/employee-times';
import { Time } from '../../../interfaces/time';

@Component({
    selector: 'app-reserve',
    templateUrl: './reserve.component.html',
    styleUrls: ['./reserve.component.scss']
})
export class ReserveComponent implements OnInit {

    @ViewChild('stepper') stepper: CustomStepperComponent;

    titles: string[] = [
        'Choose a Service',
        'Pick a Date',
        'Choose an Employee',
        'Pick a Time',
        'Leave us a Note'
    ];

    /**
     * Values to be used in the payload sent to book.
     */
    employee: EmployeeDTO;
    service: ServiceDTO;
    appointmentDate: string;
    startTime: string;
    notes: string;

    date: Date;

    modifyAppointment: boolean;
    appointment: UserAppointmentDTO;

    employees: EmployeeDTO[];
    employeesWithAvailabilities: EmployeeTimes[] = [];
    employeeShifts: ShiftDTO[];
    employeeAppointments: EmployeeAppointmentDTO[];
    chosenEmployeeAvailabilities: Time[] = [];

    /**
     * These are used to pass in values to child components (steps in the cdk stepper).
     */
    serviceSubject = new Subject<number>();
    dateSubject = new Subject<string>();
    employeeSubject = new Subject<number>();
    startTimeSubject = new Subject<string>();
    notesSubject = new Subject<string>();

    payload: BookAppointmentDTO;

    constructor(
        private appointmentService: AppointmentService,
        private serviceService: ServiceService,
        private employeeService: EmployeeService,
        private helper: HelperService,
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

    setService(service: ServiceDTO): void {
        this.service = service;
        this.stepper.nextStep();
        this.serviceSubject.next(this.service.id);
    }

    setDate(date: Date): void {
        this.date = date;
        this.stepper.nextStep();
        this.getEmployees();
        this.dateSubject.next(date.toString());
    }

    setEmployee(employee: EmployeeDTO): void {
        this.employee = employee;
        this.chosenEmployeeAvailabilities = this.employeesWithAvailabilities.find(e => e.id === this.employee.id).times;
        this.stepper.nextStep();
        this.employeeSubject.next(this.employee.id);
    }

    setTime(time: string): void {
        this.startTime = time;
        this.stepper.nextStep();
        this.startTimeSubject.next(this.startTime);
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

    getEmployees() {
        this.getAvailableEmployeesByServiceAndByDate();
    }

    getAvailableEmployeesByServiceAndByDate() {
        this.employeeService.getAvailableEmployeesByServiceAndByDate(this.service.id, this.date.toLocaleDateString()).subscribe(
            res => {
                this.employees = res;
                this.getAvailableEmployeesShiftsByDate();
            }
        );
    }

    getAvailableEmployeesShiftsByDate() {
        this.employeeService.getAvailableEmployeesShiftsByDate(this.date.toLocaleDateString()).subscribe(
            res => {
                this.employeeShifts = res;
                this.getAvailableEmployeesAppointmentsByDate();
            }
        );
    }

    getAvailableEmployeesAppointmentsByDate() {
        this.employeeService.getAvailableEmployeesAppointmentsByDate(this.date.toLocaleDateString()).subscribe(
            res => {
                this.employeeAppointments = res;
                this.findAvailabilities();
            }
        );
    }

    findAvailabilities() {

        this.employeesWithAvailabilities = [];

        this.employees.forEach(e => {

            const employee: EmployeeTimes = {
                id: e.id,
                firstName: e.firstName,
                lastName: e.lastName,
                times: this.helper.cloneTimesArray()
            };

            const shift = this.employeeShifts.find(s => s.employee.id === employee.id);
            const appointments = this.employeeAppointments.filter(a => a.employee.id === employee.id);

            employee.times.forEach(time => {
                time.enabled = this.helper.isValid(time.hour + ':' + time.minute, shift, appointments, this.service.duration, (this.modifyAppointment ? this.appointment.id : null));
            });

            this.employeesWithAvailabilities.push(employee);

        });
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
