import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ServiceService } from '../../services/service/service.service';
import { UserRegisterDTO } from '../../interfaces/user/user-register-dto';
import { UserService } from '../../services/user/user.service';
import * as countryData from 'country-telephone-data';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

    firstName: string;
    lastName: string;
    email: string;
    password: string;

    countries: Object[] = countryData.allCountries;
    selectedCountry: Object;

    areaCode: string;
    number: string;

    confirmPassword: string;
    isPasswordVisible = false;

    registerPhone = false;

    constructor(
        private userService: UserService,
        private serviceService: ServiceService,
        private router: Router,
        private googleAnalytics : GoogleAnalyticsService
    ) {
    }

    ngOnInit() {
    }

    register(): void {

        if (this.password === this.confirmPassword) {

            const payload: UserRegisterDTO = {
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email,
                password: this.password,
                phoneNumber: null
            };

            if (this.registerPhone) {
                payload.phoneNumber = {
                    countryCode: this.selectedCountry['dialCode'],
                    areaCode: this.areaCode,
                    number: this.number
                };
            }
            
            this.googleAnalytics.trackEvent('formSubmit', 'register');

            this.userService.register(payload).subscribe(
                res => {
                    const user = res['user'];
                    const token = res['token'];

                    this.userService.setUser(user);
                    this.userService.setToken(token);

                    this.router.navigate(['']);
                },
                err => console.log(err)
            );

        } else {
            alert('The passwords don\'t match.');
        }

    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }

    selectCountry(country: Object) {
        this.selectedCountry = country;
    }

}
