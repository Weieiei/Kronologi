import { Component, OnInit, Inject } from '@angular/core';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { Appointment } from 'src/app/interfaces/appointment';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-reason-dialog',
  templateUrl: './reason-dialog.component.html',
  styleUrls: ['./reason-dialog.component.scss']
})
export class ReasonDialogComponent implements OnInit {

  cancelledAppointment: Appointment;
  reasonAppointment: Appointment;
  reason: String;
  employeeName: String;
  serviceName: String;
  constructor(
    private appointmentService: AppointmentService,
    @Inject(MAT_DIALOG_DATA) cancelledAppointment: any,
    private dialogRef: MatDialogRef<ReasonDialogComponent>) {
        this.cancelledAppointment = cancelledAppointment.appointment;
        this.serviceName = cancelledAppointment.serviceName;
  }

  ngOnInit() {
      this.getCancelReason();
  }

  getCancelReason(){
      this.appointmentService.cancelAppointmentReason(1).subscribe(
        res => {
            this.reason = res["reason"]
            let canceller = res["employee"];
            this.employeeName = canceller["firstName"] + " " + canceller["lastName"];
        },
        err => console.log(err)
    );
  }

  close() {
    this.dialogRef.close();
  }
}
