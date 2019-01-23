import { Component, OnInit } from '@angular/core';
import { UpdateEmailDTO } from '../../../interfaces/user/update-email-dto';
import { UserService } from '../../../services/user/user.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UpdatePasswordDTO } from '../../../interfaces/user/update-password-dto';
import { PhoneNumberDTO } from '../../../interfaces/phonenumber/phone-number-dto';
import * as countryData from 'country-telephone-data';
import { SnackBar } from '../../../snackbar';

@Component({
    selector: 'app-account-settings',
    templateUrl: './account-settings.component.html',
    styleUrls: ['./account-settings.component.scss']
})
export class AccountSettingsComponent implements OnInit {

    // Fields to update email
    password: string;
    newEmail: string;

    // Fields to update password
    oldPassword: string;
    newPassword: string;
    confirmPassword: string;

    isPasswordVisible = false;

    hasPhoneNumber: boolean;
    countryCode: string;
    areaCode: string;
    number: string;

    countries: Object[] = countryData.allCountries;
    selectedCountry: Object;

    newAreaCode: string;
    newNumber: string;

    constructor(
        private userService: UserService,
        private snackBar: SnackBar,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.getPhoneNumber();
    }

    updateEmail(): void {

        const payload: UpdateEmailDTO = {
            password: this.password,
            newEmail: this.newEmail
        };

        this.userService.updateEmail(payload).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess(res['message']);
                this.userService.logout();
                this.router.navigate(['login']);
            },
            err => {
                if (err instanceof HttpErrorResponse && err.status === 400) {
                    this.snackBar.openSnackBarError(err.error.message);
                }
            }
        );

    }

    updatePassword(): void {

        if (this.newPassword !== this.confirmPassword) {
            this.snackBar.openSnackBarError('The passwords don\'t match.');
            return;
        }

        const payload: UpdatePasswordDTO = {
            oldPassword: this.oldPassword,
            newPassword: this.newPassword
        };

        this.userService.updatePassword(payload).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess(res['message']);
                this.userService.logout();
                this.router.navigate(['login']);
            },
            err => {
                if (err instanceof HttpErrorResponse && err.status === 400) {
                    this.snackBar.openSnackBarError(err.error.message);
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

                // If the user doesn't have a phone number saved, the response would be null
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
                this.snackBar.openSnackBarSuccess(res['message']);
            },
            err => {
                if (err instanceof HttpErrorResponse) {
                    this.snackBar.openSnackBarError(err.error.message);
                }
            }
        );

    }

    updatePhoneNumber(): void {

        const payload: PhoneNumberDTO = {
            countryCode: this.selectedCountry['dialCode'],
            areaCode: this.newAreaCode,
            number: this.newNumber
        };

        this.userService.updatePhoneNumber(payload).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess(res['message']);

                // Update current number on the page
                this.hasPhoneNumber = true;
                this.countryCode = payload.countryCode;
                this.areaCode = payload.areaCode;
                this.number = payload.number;
            },
            err => {
                if (err instanceof HttpErrorResponse) {
                    this.snackBar.openSnackBarError(err.error.message);
                }
            }
        );

    }

    selectCountry(country: Object) {
        this.selectedCountry = country;
    }
}
