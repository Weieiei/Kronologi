import { Component, OnInit } from '@angular/core';
import { UpdateEmailDTO } from '../../../interfaces/user/update-email-dto';
import { UserService } from '../../../services/user/user.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UpdatePasswordDTO } from '../../../interfaces/user/update-password-dto';
import { PhoneNumberDTO } from '../../../interfaces/phonenumber/phone-number-dto';
import * as countryData from 'country-telephone-data';
import { SnackBar } from '../../../snackbar';
import { ThemeService } from 'src/app/core/theme/theme.service';
import { TdFileUploadComponent } from '@covalent/core/file';
//npm i -save @covalent/core to  install
@Component({
    selector: 'app-account-settings',
    templateUrl: './account-settings.component.html',
    styleUrls: ['./account-settings.component.scss']
})
export class AccountSettingsComponent implements OnInit {
    // Fields to upload a profiel picture
    selectedFile: File = null;
    fileSelectMsg: string = 'No file selected yet.';
    fileUploadMsg: string = 'No file uploaded yet.';

    disabled: boolean = false;
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

    showForm: boolean;

    countries: Object[] = countryData.allCountries;
    selectedCountry: Object;

    darkModeActive: boolean;
    newAreaCode: string;
    newNumber: string;

    constructor(
        private userService: UserService,
        private snackBar: SnackBar,
        private router: Router,
        private themeService :ThemeService
    ) {
    }

    ngOnInit() {
        this.getPhoneNumber();
        this.themeService.darkModeState.subscribe(value => {
            this.darkModeActive = value;
        })
    }

    updateEmail(): void {

        if (!this.password) {
            this.snackBar.openSnackBarError('You must provide your password.');
            return;
        } else if (!this.newEmail) {
            this.snackBar.openSnackBarError('You must provide a new email.');
            return;
        }

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

        if (!this.oldPassword) {
            this.snackBar.openSnackBarError('You must provide your current password.');
            return;
        } else if (!this.newPassword) {
            this.snackBar.openSnackBarError('You must provide a new password.');
            return;
        } else if (this.newPassword !== this.confirmPassword) {
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

                this.showForm = !this.hasPhoneNumber;

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
                this.showForm = true;
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

        if (!this.selectedCountry || !this.newAreaCode || !this.newNumber) {
            this.snackBar.openSnackBarError('You must provide all fields to update your phone number.');
            return;
        }

        const payload: PhoneNumberDTO = {
            countryCode: this.selectedCountry['dialCode'],
            areaCode: this.newAreaCode,
            number: this.newNumber
        };

        this.userService.updatePhoneNumber(payload).subscribe(
            res => {
                // Update current number on the page
                this.hasPhoneNumber = true;
                this.countryCode = payload.countryCode;
                this.areaCode = payload.areaCode;
                this.number = payload.number;
                this.showForm = false;

                this.newAreaCode = void 0;
                this.newNumber = void 0;

                this.snackBar.openSnackBarSuccess(res['message']);
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

    showPhoneNumberForm() {
        this.showForm = true;
    }

    hidePhoneNumberForm() {
        this.showForm = false;
    }

    selectEvent(file: File): void {
        this.selectedFile = file;
        this.fileSelectMsg = file.name;
      }

       uploadEvent(file: File): void {
        this.fileUploadMsg = file.name;
      }

       cancelEvent(): void {
        this.fileSelectMsg = 'No file selected yet.';
        this.fileUploadMsg = 'No file uploaded yet.';
      }

      updateProfilePicture(): void {
        if (this.selectedFile != null) {
            this.userService.uploadUserPicture(this.selectedFile).subscribe(
                res => {
                    console.log('File seccessfully uploaded. ');
                    //this.fileUploadMsg = 'File seccessfully uploaded. ';
                    //get picture and show it in the profile  or update the page
                    this.router.navigate(['business']);


                },
                err => {
                    if (err instanceof HttpErrorResponse) {
                        err => console.log(err)
                    }
                }
            );
        }
    }

}
