import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PasswordMismatchStateMatcher } from '../../../shared/password-mismatch-state-matcher';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user/user.service';
import { PasswordResetDTO } from '../../interfaces/password-reset/password-reset-dto';
import { HttpErrorResponse } from '@angular/common/http';
import { SnackBar } from '../../snackbar';

@Component({
    selector: 'app-password-reset-redirect',
    templateUrl: './password-reset-redirect.component.html',
    styleUrls: ['./password-reset-redirect.component.scss']
})
export class PasswordResetRedirectComponent implements OnInit {
    isPasswordVisible = false;
    passwordRestForm: FormGroup;
    matcher: PasswordMismatchStateMatcher;
    token: string;

    constructor(
        private _formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private userService: UserService,
        private snackBar: SnackBar,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.passwordRestForm = this._formBuilder.group({
            password: [null,  [Validators.required, Validators.pattern('^(?=.*\\d)(?=.*[a-zA-Z]).{6,30}$')] ],
            confirmPassword: [null,  [Validators.required] ],
        }, {validator: this.checkPasswords});

        this.route.queryParams.subscribe(params => {
            this.token = params.token;
        });

    }

    checkPasswords(inputFormGroup: FormGroup) {
        const password = inputFormGroup.controls.password.value;
        const confirmPassword = inputFormGroup.controls.confirmPassword.value;
        return password === confirmPassword ? null : { mismatched: true };
    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }

    resetPassword() {
        if (this.token !== undefined) {
            const passwordResetValues = this.passwordRestForm.value;
            const passwordResetDto: PasswordResetDTO = {
                password: passwordResetValues.password,
                confirmPassword: passwordResetValues.confirmPassword,
                token: this.token,
            };
            this.userService.resetPassword(passwordResetDto).subscribe(
                res => {
                    this.snackBar.openSnackBarSuccess('Your password has been successfully reset');
                    this.router.navigate(['login']);
                },
                err => {
                    if (err instanceof HttpErrorResponse) {
                        this.snackBar.openSnackBarError('The system errored, please try again');
                        this.router.navigate(['login']);
                    }
                }
            );
        }

    }
}
