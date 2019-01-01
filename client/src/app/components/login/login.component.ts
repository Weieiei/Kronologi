import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    username: string;
    password: string;

    isPasswordVisible = false;

    constructor(private authService: AuthService,
                private router: Router) {
    }

    ngOnInit() {
    }

    submit() {
        this.authService.login(this.username, this.password).subscribe(
            res => {
                this.authService.setToken(res['token']);
                this.authService.verifyAdminStatus();
                this.router.navigate(['']);
            },
            err => console.log(err)
        );

    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}
