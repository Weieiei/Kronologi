import { Component, Input, OnInit } from '@angular/core';
import { MatSidenav } from '@angular/material';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';
import { AuthService } from "../../services/auth/auth.service";

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

    @Input() sidenav: MatSidenav;

    user;

    constructor(
        private userService: UserService,
        private authService: AuthService,
        private router: Router,
        private googleAnalytics: GoogleAnalyticsService
    ) {


    constructor(private userService: UserService,
                private authService: AuthService,
                private router: Router,
                private googleAnalytics: GoogleAnalyticsService) {
    }

    ngOnInit() {
        this.user = this.userService.getUser();
        this.authService.checkAdmin();
    }

    logout(): void {
        this.googleAnalytics.trackValues('security', 'logout');
        this.userService.logout();
        this.router.navigate(['login']);
    }

    goToHome() {
        this.router.navigate(['']);
    }

    goToSettings() {
        this.router.navigate(['settings']);
    }

    goToEmployeeAppointments() {
        this.router.navigate(['employee', 'appts']);
    }

    goToAdminAppointmens() {
        this.router.navigate(['admin/appts']);
    }

    goToAdminServices() {
        this.router.navigate(['admin/services']);
    }

    goToAdminUsers() {
        this.router.navigate(['admin/users']);
    }
}
