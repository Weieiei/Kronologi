import { Component, OnInit, Inject } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import { AppointmentDetailed } from 'src/app/models/appointment/AppointmentDetailed';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { UserService } from 'src/app/services/user/user.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { SnackBar } from 'src/app/snackbar';

@Component({
  selector: 'cancel-dialog',
  templateUrl: './cancel-dialog.component.html',
  styleUrls: ['./cancel-dialog.component.scss']
})
export class CancelDialogComponent implements OnInit {


  appointmentToCancel:AppointmentDetailed;
  serviceName:string;
  employeeName:string;
  customerName:string;
  isEmployee:boolean;
  isAdmin:boolean;

  constructor(
      private authService: AuthService,
      private userService: UserService,
      private appointmentService:AppointmentService,
      private dialogRef: MatDialogRef<CancelDialogComponent>,
      private snackBar: SnackBar,
      @Inject(MAT_DIALOG_DATA) appointmentToCancel:any ) {
      
      
    this.appointmentToCancel = appointmentToCancel.appointment;
    this.serviceName = appointmentToCancel.serviceName;
    this.customerName = this.appointmentToCancel.client.firstName + " " + this.appointmentToCancel.client.lastName;
    this.employeeName = this.appointmentToCancel.employee.firstName + " " + this.appointmentToCancel.employee.lastName;
    this.isEmployee = this.userService.isEmployee();
    this.isAdmin = this.authService.isAdmin();
  }

  ngOnInit() {
  }

  cancel() {
    if(!this.isEmployee){
     this.appointmentService.cancelAppointments(this.appointmentToCancel.id).subscribe(
        res => {
          this.dialogRef.close();
          this.snackBar.openSnackBarSuccess(res);
        },
        err => console.log(err)
      );
    }else{
      this.appointmentService.cancelAppointmentsEmployee(this.appointmentToCancel.id).subscribe(
        res => {
          this.dialogRef.close();
          console.log(res)
          this.snackBar.openSnackBarSuccess(res);
        },
        err => console.log(this.dialogRef.close())
      );
    }
  }

  close() {
      this.dialogRef.close();
  }
}
