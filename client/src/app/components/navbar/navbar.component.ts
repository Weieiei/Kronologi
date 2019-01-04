import { Component, Input, OnInit } from '@angular/core';
import { MatSidenav } from '@angular/material';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

    @Input() sidenav: MatSidenav;

    user;

    constructor(private userService: UserService,
                private router: Router) {
    }

    ngOnInit() {
        this.user = this.userService.getUser();
    }

    logout(): void {
        this.userService.logout();
        this.router.navigate(['login']);
    }
}
