import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AvailabilityService {

  availabilities = [
    {employee: {id: 1, name: 'Sylvia'}, availabilities: [{}]}
  ];

  constructor() { }

  getAvailabilities(): Observable<any> {
    return of(this.availabilities);
  }
}
