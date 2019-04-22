import { Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ReviewDTO } from '../../interfaces/reviewDTO';

@Injectable({
  providedIn: 'root'
})
export class ReviewService implements OnInit {

  constructor(private http: HttpClient ) {
  }

  ngOnInit() {
  }

public submitReview(payload: ReviewDTO): Observable<any> {
    return this.http.post< ReviewDTO >(['api', 'reviews'].join('/'), payload);
}
public getReviewByAppointmentId(appointmentId: number): Observable<any>  {
    return this.http.get(['api', 'reviews', appointmentId].join('/'));
}

}
