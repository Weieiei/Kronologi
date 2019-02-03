import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-appointment-card',
    templateUrl: './appointment-card.component.html',
    styleUrls: ['./appointment-card.component.scss']
})
export class AppointmentCardComponent implements OnInit {

    @Input()
    appointment: any;
    appointmentStart: Date;
    now: Date;
    constructor(private router: Router) {
    }

    ngOnInit() {
        this.now = new Date();
        this.appointmentStart = new Date(this.appointment.date + ' ' + this.appointment.startTime);
    }
    review() {
        this.router.navigate(['/review/' + this.appointment.id]);

    }
}
