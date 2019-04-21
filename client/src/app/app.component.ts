
import {Component, OnInit} from '@angular/core';
import {AuthService} from './services/auth/auth.service';
import { GoogleAnalyticsService } from './services/google/google-analytics.service';
import {ThemeService} from "./core/theme/theme.service";


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    darkModeActive :boolean;
  constructor(private authService: AuthService, private googleAnalyticsService: GoogleAnalyticsService) { }

  ngOnInit() {
    this.googleAnalyticsService.subscribe();
    this.authService.adminObservable.subscribe(res => {
        this.authService.checkAdmin();
        this.authService.checkEmployee();
        this.authService.checkClient();
    });
    this.authService.verifyAdminStatus();

  }
  ngOnDestroy() {
    // unsubscribe to the post
    this.googleAnalyticsService.unsubscribe();
  }

}
