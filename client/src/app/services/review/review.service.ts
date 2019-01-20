import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Service } from '../../models/service/Service';
import { Review } from '../../models/review/Review';
import { SubmitReview } from 'src/app/models/review/SubmitReview';

 @Injectable({
  providedIn: 'root'
})
export class ReviewService {

     constructor(private http: HttpClient) {
    }

   /*   public saveReview(review: SubmitReview): Observable<Service[]> {
        //api/user/appointments/old/review
        return this.http.post<Review>(['api', 'user', 'appointment', 'old', 'review'].join('/'));
    } */

     public saveReview(review: SubmitReview): Observable<SubmitReview> {
        return this.http.post<SubmitReview>(['api', 'user', 'appointment', 'old', 'review'].join('/'), review );
    }


 }
