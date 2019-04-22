import {Component, OnInit} from '@angular/core';
import {AppointmentService} from '../../../../services/appointment/appointment.service';
import {Appointment} from 'src/app/interfaces/appointment';
import {MatDialog, MatDialogConfig} from '@angular/material';
import {CancelDialogComponent} from 'src/app/components/cancel-dialog/cancel-dialog.component';
import {HomeComponent} from "../home.component";
import {ActivatedRoute} from "@angular/router";
import {ErrorDialogComponent} from "../../../../components/error-dialog/error-dialog.component";

@Component({
    selector: 'app-employee-appointments',
    templateUrl: './employee-appointments.component.html',
    styleUrls: ['./employee-appointments.component.scss']
})
export class EmployeeAppointmentsComponent implements OnInit {
    businessId: number;
    displayedColumns: string[] = ['service', 'date', 'time', 'duration', 'client', 'employee', 'status', 'actions'];
    displayedColumnsPastAppointments: string[] = ['service', 'date', 'time', 'duration', 'client', 'employee', 'status'];
    appointments: Appointment[];
    pastAppointments: Appointment[];


    constructor(
        private errorDialog: MatDialog,
        private route: ActivatedRoute,
        private dialog: MatDialog, private appointmentService: AppointmentService) {
        this.appointments = [];
        this.pastAppointments = [];


    }

    ngOnInit() {
        this.dialog.afterAllClosed
            .subscribe(() => {
                // update appointments when we cancel one on dialog close.
                this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"));
                this.appointments = [];
                this.getAllAppointments();
            })
    }

    getAllAppointments(): void {
        this.appointmentService.getMyAppointmentsEmployee(this.businessId).subscribe(
            res => {

                const now = new Date();

                for (const appointment of res) {
                    const appointmentStart = new Date(appointment.date + ' ' + appointment.startTime);

                    if (now <= appointmentStart) {
                        this.appointments.push(appointment);
                    } else {
                        this.pastAppointments.push(appointment);

                    }
                }
            },
            err => {
                console.log(err);
            }
        );
    }

    openDialog(element_id: any, row_id: any) {
        let appointmentToCancel: Appointment = this.appointments[row_id];
        console.log(appointmentToCancel);
        const dialogConfig = new MatDialogConfig();
        let longDescription: any;

        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;

        dialogConfig.data = {
            appointment: appointmentToCancel,
            serviceName: appointmentToCancel.service.name,
        };

        this.dialog.open(CancelDialogComponent, dialogConfig);
    }

    checkIfCancelled(row_number: any) {
        console.log(this.appointments[row_number].status);
        return this.appointments[row_number].status == 'CANCELLED';
    }

    openErrorDialog( errorMessage : any) {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = {
            title: "ERROR",
            message: errorMessage,
        };
        this.dialog.open(ErrorDialogComponent, dialogConfig);
    }
}
