import { Component, Input, OnInit } from '@angular/core';
import { Appointment } from 'src/app/models/appointment/Appointment';
import {MatDialog, MatDialogConfig} from "@angular/material";
import { CancelDialogComponent } from 'src/app/components/cancel-dialog/cancel-dialog.component';
import { Router } from '@angular/router';
import { ReviewService } from '../../../../../../services/review/review.service';
import { ReasonDialogComponent } from 'src/app/components/reason-dialog/reason-dialog.component';


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
    reviewExists = false;

    constructor(private dialog: MatDialog, private router: Router, private reviewService: ReviewService) {

    }

    ngOnInit() {
        this.now = new Date();
        this.appointmentStart = new Date(this.appointment.date + ' ' + this.appointment.startTime);
        if (this.now > this.appointmentStart) {
            this.reviewService.getReviewByAppointmentId(this.appointment.id).subscribe(
                review => {
                    if (review != null) {
                        this.reviewExists = true;
                    }
                },
            );
        }

    }

    review() {
        this.router.navigate(['/review/' + this.appointment.id]);
    }

    openDialog() {
        const dialogConfig = new MatDialogConfig();
        
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        let longDescription: string;
        dialogConfig.data = {
            appointment: this.appointment,
            serviceName: this.appointment.service.name,
            longDescription
        };
        this.dialog.open(CancelDialogComponent, dialogConfig);
    }

    openReasonDialog(){
        const dialogConfig = new MatDialogConfig();

        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = {
            appointment: this.appointment,
            serviceName: this.appointment.service.name
        };

        this.dialog.open(ReasonDialogComponent, dialogConfig);
    }
}
