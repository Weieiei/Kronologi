import { Component, OnInit } from '@angular/core';

export interface Business {
    value: string;
    viewValue: string;
}

@Component({
  selector: 'app-guest-appointment',
  templateUrl: './guest-appointment.component.html',
  styleUrls: ['./guest-appointment.component.scss']
})
export class GuestAppointmentComponent implements OnInit {
    businesses: Business[] = [
        {value: 'business-0', viewValue: 'Spa'},
        {value: 'business-1', viewValue: 'Groomer'},
        {value: 'business-2', viewValue: 'Flower Shop'}
    ];
    myFilter = (d: Date): boolean => {
        const day = d.getDay();
        // Prevent Saturday and Sunday from being selected.
        return day !== 0 && day !== 6;
    }
  constructor() { }

  ngOnInit() {
  }

}
