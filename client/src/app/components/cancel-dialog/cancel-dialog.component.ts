import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { UserService } from 'src/app/services/user/user.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { SnackBar } from 'src/app/snackbar';
import { CancelAppointmentDTO } from 'src/app/interfaces/cancelAppointmentDTO';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserAppointmentDTO } from 'src/app/interfaces/appointment/user-appointment-dto';

@Component({
  selector: 'cancel-dialog',
  templateUrl: './cancel-dialog.component.html',
  styleUrls: ['./cancel-dialog.component.scss']
})
export class CancelDialogComponent implements OnInit {
  form: FormGroup;
  toCancel: UserAppointmentDTO;
  serviceName: string;
  employeeName: string;
  customerName: string;
  isEmployee: boolean;
  isAdmin: boolean;

  constructor(
      private fb: FormBuilder,
      private authService: AuthService,
      private userService: UserService,
      private appointmentService: AppointmentService,
      private dialogRef: MatDialogRef<CancelDialogComponent>,
      private snackBar: SnackBar,
      @Inject(MAT_DIALOG_DATA) {appointment, longDescription}: any) {
    this.toCancel = appointment;
    this.serviceName = appointment.service.name;
    this.employeeName = this.toCancel.employee.firstName + ' ' + this.toCancel.employee.lastName;
    this.isEmployee = this.userService.isEmployee();
    this.isAdmin = this.authService.isAdmin();

    this.form = this.fb.group({
      longDescription: [longDescription, Validators.required]
    });
  }

  ngOnInit() {
  }

  cancel() {
    if (!this.isEmployee) {
      const payload: CancelAppointmentDTO = {
        reason: 'Cancelled by customer',
        idOfAppointment: this.toCancel.id,
    };
     this.appointmentService.cancelAppointments(this.toCancel.id, payload).subscribe(
        res => {
          this.dialogRef.close();
          this.snackBar.openSnackBarSuccess(res['message']);
        },
        err => console.log(err)
      );
    } else {
      const payload: CancelAppointmentDTO = {
        reason: this.form.value.longDescription,
        idOfAppointment: this.toCancel.id,
    };

      this.appointmentService.cancelAppointmentsEmployee(payload).subscribe(
        res => {
          this.dialogRef.close();
          this.snackBar.openSnackBarSuccess(res['message']);
        },
        err => console.log(this.dialogRef.close())
      );
    }
  }

  close() {
      this.dialogRef.close();
  }
}
