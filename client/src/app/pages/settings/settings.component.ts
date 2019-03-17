import { Component, OnInit } from '@angular/core';
import { ThemeService } from "../../core/theme/theme.service";

@Component({
    selector: 'app-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

    darkModeActive: boolean;

    constructor(private themeService: ThemeService) {
    }

    ngOnInit() {
        this.themeService.darkModeState.subscribe(value => {
            this.darkModeActive = value;
        })
    }

}
