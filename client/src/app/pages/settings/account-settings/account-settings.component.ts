import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-account-settings',
    templateUrl: './account-settings.component.html',
    styleUrls: ['./account-settings.component.scss']
})
export class AccountSettingsComponent implements OnInit {

    oldPassword: string;
    newPassword: string;
    confirmPassword: string;

    isPasswordVisible = false;

    newEmail: string;

    constructor() {
    }

    ngOnInit() {
    }

    changePassword(): void {
        // todo
    }

    changeEmail(): void {
        // todo
    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}
