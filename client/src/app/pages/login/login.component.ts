import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user/user.service';
import { UserLoginDTO } from '../../interfaces/user/user-login-dto';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';
import { AuthService } from "../../services/auth/auth.service";
import { MatDialog, MatDialogConfig } from '@angular/material';
import { PasswordForgotDialogComponent } from '../../components/password-forgot-dialog/password-forgot-dialog.component';
import { SnackBar } from '../../snackbar';
import {ErrorDialogComponent} from "../../components/error-dialog/error-dialog.component";


@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    username: string;
    password: string;

    isPasswordVisible = false;

    constructor(private userService: UserService,
                private router: Router,
                private googleAnalytics:  GoogleAnalyticsService,
                private dialog: MatDialog,
                private snackBar: SnackBar,
                private errorDialog: MatDialog,

    ) {
    }

    ngOnInit() {
    }

    submit() {
        const payload: UserLoginDTO = {
            email: this.username,
            password: this.password
        };

        this.userService.login(payload).subscribe(
            res => {
                this.googleAnalytics.trackValues('security', 'login', 'success');
                const token = res['token'];
                this.userService.setToken(token);
                let businessId = this.userService.getBusinessIdFromToken();

                if (this.userService.isAdmin()){
                    this.router.navigate([businessId.toString()+'/admin/appts']);
                }
                else if (this.userService.isEmployee()){
                    this.router.navigate([businessId.toString()+'/employee/appts']);
                }
                else {
                    this.router.navigate(['business']);
                }



            },
            err => {
                this.openErrorDialog(err["status"]);
                this.googleAnalytics.trackValues('security', 'login', 'failure');
                console.log(err);
            }
        );
    }

    openDialog() {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = {
            email: this.username,
        };
        this.dialog.open(PasswordForgotDialogComponent, dialogConfig);
        this.dialog.afterAllClosed
            .subscribe(() => {
                this.snackBar.openSnackBarSuccess('A password request message has been sent');
            });
    }

    openErrorDialog(errorMessage: any) {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = {
            title: "ERROR",
            messageOrStatus: errorMessage,
        };
        this.dialog.open(ErrorDialogComponent, dialogConfig);
    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}
