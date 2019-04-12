import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user/user.service";
import { Router } from "@angular/router";
import {UserRegisterDTO} from "../../interfaces/user/user-register-dto";
import * as countryData from 'country-telephone-data';
import {PhoneNumberDTO} from "../../interfaces/phonenumber/phone-number-dto";

@Component({
  selector: 'app-guest-appointment',
  templateUrl: './guest-appointment.component.html',
  styleUrls: ['./guest-appointment.component.scss']
})
export class GuestAppointmentComponent implements OnInit {

    firstName: string;
    lastName: string;
    email: string;
    countries: Object[] = countryData.allCountries;
    selectedCountry: Object;
    areaCode: string;
    number: string;
    registerPhone = false;

  constructor(
      private userService: UserService,
      private router: Router
  ) { }

  ngOnInit() {
  }

  reserve() {
        const payload: UserRegisterDTO = {
            firstName: this.firstName,
            lastName: this.lastName,
            email: this.email,
            password: 'temp',
            phoneNumber: null
        };
        this.userService.register_guest(payload).subscribe(
            res => {
                const token = res['token'];
                this.userService.setToken(token);
                this.router.navigate(['appointments']);
            },
            err => {
                console.log(err);
            }
        );

  }

}
