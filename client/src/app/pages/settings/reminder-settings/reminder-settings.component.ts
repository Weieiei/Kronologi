import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user/user.service';
import { UpdateSettingsDTO } from '../../../interfaces/update-settings-dto';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-reminder-settings',
    templateUrl: './reminder-settings.component.html',
    styleUrls: ['./reminder-settings.component.scss']
})
export class ReminderSettingsComponent implements OnInit {

    emailReminder: boolean;
    textReminder: boolean;

    successMessage: string;
    failMessage: string;

    constructor(private userService: UserService) {
    }

    ngOnInit() {
        this.getSettings();
    }

    getSettings(): void {
        this.userService.getSettings().subscribe(
            res => {
                this.emailReminder = res.emailReminder;
                this.textReminder = res.textReminder;
            },
            err => console.log(err)
        );
    }

    updateSettings(): void {

        const payload: UpdateSettingsDTO = {
            emailReminder: this.emailReminder,
            textReminder: this.textReminder
        };

        this.userService.updateSettings(payload).subscribe(
            res => {
                this.failMessage = void 0;
                this.successMessage = res['message'];
            },
            err => {
                this.successMessage = void 0;
                if (err instanceof HttpErrorResponse) {
                    this.failMessage = err.error.message;
                }
            }
        );
    }

}
