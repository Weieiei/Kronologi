import { Component, OnInit } from '@angular/core';
import { GuestService } from "../../services/guest/guest.service";
import { Router } from "@angular/router";
import * as countryData from 'country-telephone-data';
import {PhoneNumberDTO} from "../../interfaces/phonenumber/phone-number-dto";
import {GuestCreateDto} from "../../interfaces/guest/guest-create-dto";
import { BookAppointmentDTO } from '../../interfaces/appointment/book-appointment-dto';

@Component({
  selector: 'app-guest',
  templateUrl: './guest.component.html',
  styleUrls: ['./guest.component.scss']
})
export class GuestComponent implements OnInit {

    firstName: string;
    lastName: string;
    email: string;
    countries: Object[] = countryData.allCountries;
    selectedCountry: Object;
    areaCode: string;
    number: string;
    registerPhone = false;

  constructor(
      private guestService: GuestService,
      private router: Router
  ) { }

  ngOnInit() {
  }

  register() {
  }

  reserve() {
        const payload: GuestCreateDto = {
            firstName: this.firstName,
            lastName: this.lastName,
            email: this.email,
            phoneNumber: null,
            appointment: null
  };
        this.guestService.register_guest(payload).subscribe(
            res => {
                const token = res['token'];
                this.guestService.setToken(token);
                this.router.navigate(['appointments']);
            },
            err => {
                console.log(err);
            }
        );

  }

}
