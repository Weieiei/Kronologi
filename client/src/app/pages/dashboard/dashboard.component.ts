import { Component, OnInit } from '@angular/core';
import {ThemeService} from "../../core/theme/theme.service";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
    darkModeActive: boolean;
    constructor(private themeService: ThemeService) {
    }

    ngOnInit() {
        this.themeService.darkModeState.subscribe(value => {
            this.darkModeActive = value;
        })
    }
}
