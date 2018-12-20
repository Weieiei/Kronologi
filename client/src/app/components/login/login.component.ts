import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Router } from '@angular/router';
import { UserLogin } from '../../models/user/UserLogin';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    /**
     * Using RegisterComponent's css since they're pretty much the same component.
     */
    styleUrls: ['../register/register.component.scss']
})
export class LoginComponent implements OnInit {

    user: UserLogin;

    username: string;
    password: string;

    @ViewChild('usernameInput') usernameInput: ElementRef;

    constructor(
        private authService: AuthService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.usernameInput.nativeElement.focus();
    }

    loginUser() {

        this.user = new UserLogin(this.username, this.password);

        this.authService.loginUser(this.user).subscribe(
            res => {
                this.authService.setToken(res['token']);
                this.authService.verifyAdminStatus();
                this.router.navigate(['']);
            },
            err => console.log(err)
        );

    }

}
