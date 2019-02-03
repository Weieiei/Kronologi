import { Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ReviewDTO } from '../../interfaces/reviewDTO';

@Injectable({
  providedIn: 'root'
})
export class ReviewService implements OnInit {

  constructor(private http: HttpClient, private _Activatedroute: ActivatedRoute ) {
      // console.log(this._Activatedroute.snapshot.params['apptmtId']);
  }

  ngOnInit() {
  }

// need to create model in client folder for ReviewToSubmit
public submitReview(payload: ReviewDTO): Observable<any> {
    return this.http.post< ReviewDTO >(['api', 'reviews'].join('/'), payload);
}

}
