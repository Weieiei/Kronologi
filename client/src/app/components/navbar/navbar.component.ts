import { Component, Input, OnInit } from '@angular/core';
import { MatSidenav } from '@angular/material';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';
import { AuthService } from '../../services/auth/auth.service';
import { ThemeService } from "../../core/theme/theme.service";
import {OverlayContainer} from "@angular/cdk/overlay";
import { DomSanitizer} from '@angular/platform-browser';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
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
    theme :string = 'dark-theme';
    imagePath :string = "";

    imageToShow: any;
    isImageLoading: any;
    constructor(
        private sanitizer: DomSanitizer,
        private userService: UserService,
        private authService: AuthService,
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
        this.overlayContainer.getContainerElement().classList.add(this.theme);

        this.userService.getUserProfile().subscribe(
          data => {
              if ( data ) {
                this.imagePath = 'data:image/png;base64,' + data["image_encoded"];
                 // show data base64:
                 // console.log(this.imagePath);
                this.sanitizedImageData = this.sanitizer.bypassSecurityTrustUrl(this.imagePath);
              } else {
                        this.sanitizedImageData = 'assets/images/user_default.png';
                        console.log(this.sanitizedImageData);
                    }

          },


                 err => console.log(err)
        );

    }


    onThemeChange(theme:string) {
        this.theme = theme;
        //console.log(theme);
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
        const currentTheme: string = this.darkModeActive ? 'dark-theme': '';
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
}
