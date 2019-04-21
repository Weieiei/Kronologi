import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../services/user/user.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { SnackBar } from '../../snackbar';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-password-forgot-dialog',
  templateUrl: './password-forgot-dialog.component.html',
  styleUrls: ['./password-forgot-dialog.component.scss']
})
export class PasswordForgotDialogComponent implements OnInit {

    passwordForgotForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private userService: UserService,
        private dialogRef: MatDialogRef<PasswordForgotDialogComponent>,
        private snackBar: SnackBar,
        @Inject(MAT_DIALOG_DATA) {email}: any) {
            this.passwordForgotForm = this.fb.group({
                email: [email, [Validators.required, Validators.email]]
        });
    }

    ngOnInit() {
    }

    sendResetEmail() {
       this.userService.sendPasswordResetEmail(this.passwordForgotForm.value.email).subscribe(
           res => {
               this.dialogRef.close();
           },
           err => {
                if (err instanceof HttpErrorResponse) {
                    this.dialogRef.close();
                }
           }
       );
    }

    close() {
        this.dialogRef.close();
    }
}
