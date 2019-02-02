import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReviewService } from '../../services/review/review.service';
import { Review } from 'src/app/interfaces/review';
import { User } from 'src/app/models/user/User';
@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit {

    review: string;
    client_id: number;
    appointment_id: number;

  constructor(
    private reviewService: ReviewService,
    private router: Router
) {
}

   ngOnInit() {
  }

   saveReview(): void {

    const payload: Review = {
        review: this.review
        // after the GET
        // client_id:
        // appointment_id:

    };
    this.reviewService.submitReview(payload).subscribe(
        res => {

            this.router.navigate(['']);
        },
        err => console.log(err)
    );
  }
}
