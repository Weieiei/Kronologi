import { Component, OnInit } from '@angular/core';
import { NewEmailDTO } from '../../../interfaces/new-email-dto';
import { UserService } from '../../../services/user/user.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { NewPasswordDTO } from '../../../interfaces/new-password-dto';

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

    hasPhoneNumber: boolean;
    countryCode: string;
    areaCode: string;
    number: string;

    successMessagePhone: string;
    errorMessagePhone: string;

    constructor(
        private userService: UserService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.getPhoneNumber();
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

        if (this.newPassword !== this.confirmPassword) {
            this.updatePasswordErrorMessage = 'The passwords don\'t match.';
            return;
        }

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

    getPhoneNumber(): void {
        this.userService.getPhoneNumber().subscribe(
            res => {

                // User might not have a phone number saved, so the returned phone number might just be null
                if (res) {
                    this.hasPhoneNumber = true;
                    this.countryCode = res.countryCode;
                    this.areaCode = res.areaCode;
                    this.number = res.number;
                } else {
                    this.hasPhoneNumber = false;
                }

            },
            err => console.log(err)
        );
    }

    deletePhoneNumber(): void {

        if (!confirm('Click OK to delete your phone number.')) {
            return;
        }

        this.userService.deletePhoneNumber().subscribe(
            res => {
                this.hasPhoneNumber = false;
                this.errorMessagePhone = void 0;
                this.successMessagePhone = res['message'];
            },
            err => {
                this.successMessagePhone = void 0;
                if (err instanceof HttpErrorResponse) {
                    this.errorMessagePhone = err.error.message;
                }
            }
        );

    }
}
