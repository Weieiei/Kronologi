import { Component, OnInit } from '@angular/core';
import { NewEmailDTO } from '../../../interfaces/new-email-dto';
import { UserService } from '../../../services/user/user.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import {NewPasswordDTO} from "../../../interfaces/new-password-dto";

@Component({
    selector: 'app-account-settings',
    templateUrl: './account-settings.component.html',
    styleUrls: ['./account-settings.component.scss']
})
export class AccountSettingsComponent implements OnInit {

    // Fields to update email
    password: string;
    newEmail: string;

    updateEmailErrorMessage: string;

    // Fields to update password
    oldPassword: string;
    newPassword: string;
    confirmPassword: string;

    isPasswordVisible = false;

    updatePasswordErrorMessage: string;

    constructor(
        private userService: UserService,
        private router: Router
    ) {
    }

    ngOnInit() {
    }

    updateEmail(): void {

        const payload: NewEmailDTO = {
            password: this.password,
            newEmail: this.newEmail
        };

        this.userService.updateEmail(payload).subscribe(
            res => {
                this.userService.logout();
                this.router.navigate(['login']);
            },
            err => {
                if (err instanceof HttpErrorResponse && err.status === 400) {
                    this.updateEmailErrorMessage = err.error.message;
                }
            }
        );

    }

    updatePassword(): void {

        const payload: NewPasswordDTO = {
            oldPassword: this.oldPassword,
            newPassword: this.newPassword
        };

        this.userService.updatePassword(payload).subscribe(
            res => {
                this.userService.logout();
                this.router.navigate(['login']);
            },
            err => {
                if (err instanceof HttpErrorResponse && err.status === 400) {
                    this.updatePasswordErrorMessage = err.error.message;
                }
            }
        );

    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}
