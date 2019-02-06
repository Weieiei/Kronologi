import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../../../services/appointment/appointment.service';
import { Appointment } from 'src/app/interfaces/appointment';
import { MatDialogConfig, MatDialog } from '@angular/material';
import { AppointmentDetailed } from 'src/app/models/appointment/AppointmentDetailed';
import { CancelDialogComponent } from 'src/app/components/cancel-dialog/cancel-dialog.component';

@Component({
    selector: 'app-employee-appointments',
    templateUrl: './employee-appointments.component.html',
    styleUrls: ['./employee-appointments.component.scss']
})
export class EmployeeAppointmentsComponent implements OnInit {

    displayedColumns: string[] = ['service', 'date', 'time', 'duration', 'client', 'employee', 'status', 'actions'];
    appointments: Appointment[];
    pastAppointments: Appointment[];

    constructor(private dialog: MatDialog, private appointmentService: AppointmentService) {
        this.appointments = [];
        this.pastAppointments = [];
    }

    ngOnInit() {
        this.getAllAppointments();
        
    }

    getAllAppointments(): void {
        this.appointmentService.getMyAppointmentsEmployee().subscribe(
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
            err => console.log(err)
        );
    }

    openDialog(row_id:any){
        let appointmentToCancel: Appointment = this.appointments[row_id-1];
        const dialogConfig = new MatDialogConfig();
        let longDescription: any;
        
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
    
        dialogConfig.data = {
            appointment: appointmentToCancel,
            serviceName: appointmentToCancel.service.name,
            longDescription
        };

        this. dialog.afterAllClosed
        .subscribe(() => {
        //update appointments when we cancel one on dialog close.
          this.appointments=[];
          this.getAllAppointments();
        })
        this.dialog.open(CancelDialogComponent, dialogConfig);
    }

    checkIfCancelled(row_id:any){
        return this.appointments[row_id-1].status == 'cancelled'
    }
}
