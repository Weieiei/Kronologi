import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from '../../../services/review/review.service';
import { ReviewDTO } from 'src/app/interfaces/reviewDTO';
import { SnackBar } from "../../../snackbar";
import { ThemeService } from "../../../core/theme/theme.service";

@Component({
    selector: 'app-review',
    templateUrl: './review.component.html',
    styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit {

    content: string;
    appointmentId: number;
    darkModeActive: boolean;

    constructor(
        private snackBar: SnackBar,
        private reviewService: ReviewService,
        private router: Router,
        private _Activatedroute: ActivatedRoute,
        private themeService: ThemeService
    ) {
    }

    ngOnInit() {
        console.log(this._Activatedroute.snapshot.params['apptmtId']);
        this.appointmentId = this._Activatedroute.snapshot.params['apptmtId'];
        this.themeService.darkModeState.subscribe(value => {
            this.darkModeActive = value;
        })
    }

    saveReview(): void {

        const payload: ReviewDTO = {
            content: this.content,
            appointmentId: this.appointmentId
        };
        this.reviewService.submitReview(payload).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess(res["message"]);
                this.router.navigate(['']);
            },
            err => console.log(err)
        );
    }
}
