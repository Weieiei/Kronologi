import { Component, Input, OnInit } from '@angular/core';
import { MatSidenav } from '@angular/material';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';
import { AuthService } from '../../services/auth/auth.service';
import { ThemeService } from "../../core/theme/theme.service";

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

    @Input() sidenav: MatSidenav;
    
    userEmail = "";
    userName = "";
    route = "";
    showMenu = false;
    darkModeActive: boolean;
    user;

    constructor(
        private userService: UserService,
        private authService: AuthService,
        private router: Router,
        private googleAnalytics: GoogleAnalyticsService,
        public themeService: ThemeService

    ) {
    }

    ngOnInit() {
        // this.user = this.userService.getUser();
        this.authService.checkAdmin();
        this.userName = this.userService.getFirstNameFromToken() + " " + this.userService.getLastNameFromToken();
        this.userEmail = this.userService.getEmailFromToken();
        this.themeService.darkModeState.subscribe(value => {
            this.darkModeActive = value;
        });
    }

    logout(): void {
        this.googleAnalytics.trackValues('security', 'logout');
        this.userService.logout();
        this.router.navigate(['login']);
    }

    modeToggleSwitch() {
        this.themeService.darkModeState.next(!this.darkModeActive)
    }

    toggleMenu() {
        this.showMenu = !this.showMenu;
      }

    goToHome() {
        this.router.navigate(['business']);
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

    goToEmployees() {
        this.router.navigate(['admin', 'employees']);
    }
}
