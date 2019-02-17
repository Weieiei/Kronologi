import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class VerificationService {

    constructor(private http: HttpClient) {
    }

    verify(hash): Observable<HttpResponse<any>> {
        const params = new HttpParams().set('hash', hash);
        return this.http.get<HttpResponse<any>>('api/user/verification',  { params, observe: 'response' });
    }
}
