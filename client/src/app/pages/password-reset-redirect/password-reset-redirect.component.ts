import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PasswordMismatchStateMatcher } from '../../../shared/password-mismatch-state-matcher';

@Component({
    selector: 'app-password-reset-redirect',
    templateUrl: './password-reset-redirect.component.html',
    styleUrls: ['./password-reset-redirect.component.scss']
})
export class PasswordResetRedirectComponent implements OnInit {
    isPasswordVisible = false;
    passwordRestForm: FormGroup;
    matcher: PasswordMismatchStateMatcher;

    constructor(
        private _formBuilder: FormBuilder,
    ) {
    }

    ngOnInit() {
        this.passwordRestForm = this._formBuilder.group({
            password: [null,  [Validators.required, Validators.pattern('^(?=.*\\d)(?=.*[a-zA-Z]).{6,30}$')] ],
            confirmPassword: [null,  [Validators.required] ],
        }, {validator: this.checkPasswords});

    }

    checkPasswords(inputFormGroup: FormGroup) {
        const password = inputFormGroup.controls.password.value;
        const confirmPassword = inputFormGroup.controls.confirmPassword.value;
        return password === confirmPassword ? null : { mismatched: true };
    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}
