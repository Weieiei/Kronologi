import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-reminder-settings',
    templateUrl: './reminder-settings.component.html',
    styleUrls: ['./reminder-settings.component.scss']
})
export class ReminderSettingsComponent implements OnInit {

    emailReminder: boolean;
    textReminder: boolean;

    constructor() {
    }

    ngOnInit() {
    }

    updateReminders(): void {
        console.log(this.emailReminder);
        console.log(this.textReminder);
    }

}
