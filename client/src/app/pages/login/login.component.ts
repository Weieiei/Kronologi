import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user/user.service';
import { UserLoginDTO } from '../../interfaces/user/user-login-dto';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';
import { AuthService } from "../../services/auth/auth.service";


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
                private googleAnalytics :  GoogleAnalyticsService) {
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

                if (this.userService.isAdmin()){
                    this.router.navigate(['admin/appts']);
                }
                else if (this.userService.isEmployee()){
                    this.router.navigate(['employee/appts']);
                }
                else {
                    this.router.navigate(['business']);
                }



            },
            err => {
                this.googleAnalytics.trackValues('security', 'login', 'failure');
                console.log(err);
            }
        );

    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}
