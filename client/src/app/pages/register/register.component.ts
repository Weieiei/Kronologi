import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ServiceService } from '../../services/service/service.service';
import { UserRegisterDTO } from '../../interfaces/user-register-dto';
import { UserService } from '../../services/user/user.service';
import { Observable } from 'rxjs';
import { FormControl } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import * as countryData from 'country-telephone-data';

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

    countryCode: string;
    areaCode: string;
    number: string;

    countryControl = new FormControl();
    countries: Object[] = countryData.allCountries;
    filteredCountries: Observable<Object[]>;

    confirmPassword: string;
    isPasswordVisible = false;

    registerPhone = false;

    constructor(
        private userService: UserService,
        private serviceService: ServiceService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.filteredCountries = this.countryControl.valueChanges.pipe(
            startWith(''),
            map(value => this.filterCountries(value))
        );
    }

    filterCountries(value: string): Object[] {
        const filterValue = value.toLowerCase();
        return this.countries.filter(country => country['name'].toLowerCase().indexOf(filterValue) === 0);
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
                    countryCode: this.countryCode,
                    areaCode: this.areaCode,
                    number: this.number
                };
            }

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

    selectCountry(countryName: string) {
        this.countryCode = '+' + this.countries.find(country => country['name'] === countryName)['dialCode'];
    }

}
