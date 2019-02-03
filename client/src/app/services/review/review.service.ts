import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient) { }
// need to create model in client folder for ReviewToSubmit
public submitReview(review: ReviewToSubmit): Observable<any> {
    return this.http.post< ReviewToSubmit >(['api', 'user', 'reviews'].join('/'), review);
}

}
