import { Component, Input, OnInit } from '@angular/core';
import { Appointment } from 'src/app/models/appointment/Appointment';
import {MatDialog, MatDialogConfig} from "@angular/material";
import { CancelDialogComponent } from 'src/app/components/cancel-dialog/cancel-dialog.component';


@Component({
    selector: 'app-appointment-card',
    templateUrl: './appointment-card.component.html',
    styleUrls: ['./appointment-card.component.scss']
})
export class AppointmentCardComponent implements OnInit {

    @Input()
    appointment: any;
    appointmentStart: Date;
    now: Date;
    constructor(private dialog: MatDialog) {
    }

    ngOnInit() {
        this.now = new Date();
        this.appointmentStart = new Date(this.appointment.date + ' ' + this.appointment.startTime);
    }

    openDialog() {
        const dialogConfig = new MatDialogConfig();
        
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
    
        dialogConfig.data = {
            appointment: this.appointment,
            serviceName: this.appointment.service.name
        };
        this.dialog.open(CancelDialogComponent, dialogConfig);
    }
}
