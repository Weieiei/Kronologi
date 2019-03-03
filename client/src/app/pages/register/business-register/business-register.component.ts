import { Component, OnInit } from '@angular/core';
import { NONE_TYPE } from '@angular/compiler/src/output/output_ast';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ServiceService } from '../../../services/service/service.service';
import { BusinessService } from '../../../services/business/business.service';
import { UserRegisterDTO } from '../../../interfaces/user/user-register-dto';
import { BusinessRegisterDTO } from '../../../interfaces/business/business-register-dto';
import { UserService } from '../../../services/user/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as countryData from 'country-telephone-data';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';
import { ServiceCreateDto } from '../../../interfaces/service/service-create-dto';

@Component({
  selector: 'app-business-register',
  templateUrl: './business-register.component.html',
  styleUrls: ['./business-register.component.scss']
})
export class BusinessRegisterComponent implements OnInit {

  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;

    selectedFile: File = null;
// new business object
    businessName: string;
    selectedDomain: string;
    description: string;
// new service object
    service: string;
    service_duration: number;
//new user object
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
        private http: HttpClient,
        private router: Router,
        private _formBuilder: FormBuilder,
        private userService: UserService,
        private serviceService: ServiceService,
        private googleAnalytics: GoogleAnalyticsService,
        private businessService: BusinessService,
        private serviceCreateDto: ServiceCreateDto
         ) { }

    ngOnInit() {
        this.firstFormGroup = this._formBuilder.group({
          firstCtrl: ['', Validators.required]
        });
        this.secondFormGroup = this._formBuilder.group({
          secondCtrl: ['', Validators.required]
        });
      }

    onFileSelected(event) {
        this.selectedFile = <File> event.target.files[0];
    }
    onUpload()  {
        const  fd = new FormData();
        fd.append('image', this.selectedFile, this.selectedFile.name );
        this.http.post('https://url', fd )
                .subscribe(
                    response => {
                    console.log(response);
                });
    }
    business_register() {
        // register business
        const payload_business: BusinessRegisterDTO = {
            businessName: this.secondFormGroup.controls['businessName'].value,
            domain: this.secondFormGroup.controls['selectedDomain'].value,
            description: this.secondFormGroup.controls['description'].value
        };
        this.businessService.createBusiness(payload_business).subscribe(
            res => {

            },
            err => console.log(err)
        );
        //create new service
        const payload_service: ServiceCreateDto = {
            id: 0,  // why we have id in the ServiceCreateDto??? I will add for now
            name: this.secondFormGroup.controls['service'].value,
            duration: this.secondFormGroup.controls['service_duration'].value,
            };
        this.businessService.createBusiness(payload_business).subscribe(
            res => {

            },
            err => console.log(err)
        );
// TODO: when register user, we need to add business id, also need to link service to the user
        if (this.firstFormGroup.controls['password'].value === this.firstFormGroup.controls['confirmPassword'].value ) {

            const payload: UserRegisterDTO = {
                firstName: this.firstFormGroup.controls['firstName'].value,
                lastName: this.firstFormGroup.controls['lastName'].value,
                email: this.firstFormGroup.controls['email'].value,
                password: this.firstFormGroup.controls['password'].value,
                phoneNumber: null
            };

            if ( this.registerPhone) {
                payload.phoneNumber = {
                    countryCode: this.firstFormGroup.controls['selectedCountry'].value['dialCode'],
                    areaCode: this.firstFormGroup.controls['areaCode'].value,
                    number: this.firstFormGroup.controls['number'].value
                };
            }

            this.googleAnalytics.trackValues('formSubmit', 'register');

            this.userService.register(payload).subscribe(
                res => {
                    // this.router.navigate(['login']);
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
