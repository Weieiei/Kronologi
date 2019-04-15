import { Component, Input, OnInit } from '@angular/core';
import { MatSidenav } from '@angular/material';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';
import { AuthService } from '../../services/auth/auth.service';
import { ThemeService } from "../../core/theme/theme.service";
import { OverlayContainer } from "@angular/cdk/overlay";
import { DomSanitizer} from '@angular/platform-browser';
import {AuthGuard} from "../../guards/auth/auth.guard";

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {

    @Input() sidenav: MatSidenav;

    sanitizedImageData: any;
    userEmail = "";
    userName = "";
    route = "";
    showMenu = false;
    darkModeActive: boolean;
    user;
    dark_theme :string = 'dark-theme';
    light_theme:string = 'light-theme';
    theme:string = 'light-theme';
    imagePath :string = "";

    imageToShow: any;
    isImageLoading: any;
    constructor(
        private sanitizer: DomSanitizer,
        public userService: UserService,
        public authService: AuthService,
        public authGuard: AuthGuard,
        private router: Router,
        private googleAnalytics: GoogleAnalyticsService,
        public themeService: ThemeService,
        private overlayContainer: OverlayContainer

    ) {

    }

    ngOnInit() {
        this.authService.checkAdmin();
        this.userName = this.userService.getFirstNameFromToken() + " " + this.userService.getLastNameFromToken();
        this.userEmail = this.userService.getEmailFromToken();
        this.themeService.darkModeState.subscribe(value => {
            this.darkModeActive = value;
        });
        this.overlayContainer.getContainerElement().classList.add(this.dark_theme);
        this.overlayContainer.getContainerElement().classList.add(this.light_theme);

        this.userService.getUserProfile().subscribe(
          data => {
              if ( data ) {
                this.imagePath = 'data:image/png;base64,' + data["image_encoded"];
                this.sanitizedImageData = this.sanitizer.bypassSecurityTrustUrl(this.imagePath);
              } else {
                        this.sanitizedImageData = 'assets/images/user_default.png';
                    }

          },


                 err => console.log(err)
        );

    }

    onThemeChange(theme:string) {
        this.theme = theme;
        const overlayContainerClasses = this.overlayContainer.getContainerElement().classList;
        const themeClassesToRemove = Array.from(overlayContainerClasses).filter((item: string) => item.includes('-theme'));
        if (themeClassesToRemove.length) {
            overlayContainerClasses.remove(...themeClassesToRemove);
        }
        overlayContainerClasses.add(theme);
    }

    logout(): void {
        this.googleAnalytics.trackValues('security', 'logout');
        this.userService.logout();
        this.router.navigate(['login']);
    }

    modeToggleSwitch() {
        this.darkModeActive = !this.darkModeActive;
        this.themeService.darkModeState.next(this.darkModeActive);
        const currentTheme: string = this.darkModeActive ? 'light-theme': 'dark-theme';
        this.onThemeChange(currentTheme);
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

    goToSyncCalendars(){
        this.router.navigate(['syncCalendars']);
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

    isLoggedIn() {
        return this.authGuard.canActivate();
    }

    isNotLoggedIn() {
        return !this.isLoggedIn();
    }
}
