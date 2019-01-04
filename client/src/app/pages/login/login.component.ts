import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user/user.service';

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
                private router: Router) {
    }

    ngOnInit() {
    }

    submit() {
        this.userService.login(this.username, this.password).subscribe(
            res => {
                const user = res['user'];
                const token = res['token'];

                this.userService.setUser(user);
                this.userService.setToken(token);

                this.router.navigate(['']);
            },
            err => console.log(err)
        );

    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}
