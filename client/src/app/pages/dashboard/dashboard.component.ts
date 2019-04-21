import { Component, OnInit } from '@angular/core';
import {ThemeService} from "../../core/theme/theme.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
    darkModeActive: boolean;
    constructor(private themeService: ThemeService, private router: Router,) {
    }

    ngOnInit() {
        this.themeService.darkModeState.subscribe(value => {
            this.darkModeActive = value;
            this.router.navigate(['business'])
        })
    }
}
