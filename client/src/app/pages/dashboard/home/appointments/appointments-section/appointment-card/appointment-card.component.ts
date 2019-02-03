import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReviewService } from '../../../../../../services/review/review.service';

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
    invalid = true;

    constructor(private router: Router, private reviewService: ReviewService) {
    }

    ngOnInit() {
        this.now = new Date();
        this.appointmentStart = new Date(this.appointment.date + ' ' + this.appointment.startTime);
        this.reviewService.getReviewByAppointmentId(this.appointment.id).subscribe(
           found => {
               if (found.content == null) {

               } else {
                this.invalid = false;
               }
           },

         );
    }
    review() {

        this.router.navigate(['/review/' + this.appointment.id]);
    }
    isInvalid(): boolean {
        return !this.invalid;
    }
}
