import { Component, OnInit } from '@angular/core';
import { NewEmailDTO } from '../../../interfaces/new-email-dto';
import { UserService } from '../../../services/user/user.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-account-settings',
    templateUrl: './account-settings.component.html',
    styleUrls: ['./account-settings.component.scss']
})
export class AccountSettingsComponent implements OnInit {

    newEmail: string;

    oldPassword: string;
    newPassword: string;
    confirmPassword: string;

    isPasswordVisible = false;

    errorMessage: string;

    constructor(
        private userService: UserService,
        private router: Router
    ) {
    }

    ngOnInit() {
    }

    changeEmail(): void {

        const payload: NewEmailDTO = {
            newEmail: this.newEmail
        };

        this.userService.updateEmail(payload).subscribe(
            res => {
                this.userService.logout();
                this.router.navigate(['login']);
            },
            err => {
                if (err instanceof HttpErrorResponse && err.status === 400) {
                    this.errorMessage = err.error.message;
                }
            }
        );

    }

    changePassword(): void {
        // todo
    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}
