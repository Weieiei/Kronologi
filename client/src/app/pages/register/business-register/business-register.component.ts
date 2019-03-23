import { Component, OnInit } from '@angular/core';
import { NONE_TYPE } from '@angular/compiler/src/output/output_ast';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ServiceService } from '../../../services/service/service.service';
import { BusinessService } from '../../../services/business/business.service';
import { BusinessUserRegisterDTO } from '../../../interfaces/user/business-user-register-dto';
import { BusinessRegisterDTO } from '../../../interfaces/business/business-register-dto';
import { BusinessDTO } from '../../../interfaces/business/business-dto';
import { UserService } from '../../../services/user/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as countryData from 'country-telephone-data';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';
import { ServiceCreateDto } from '../../../interfaces/service/service-create-dto';

export interface Domain {
    value: string;
  }
@Component ({
  selector: 'app-business-register',
  templateUrl: './business-register.component.html',
  styleUrls: ['./business-register.component.scss']
})
export class BusinessRegisterComponent implements OnInit {

    firstFormGroup: FormGroup;
    secondFormGroup: FormGroup;
    thirdFormGroup: FormGroup;
    selectedFile: File = null;
// new business object
    business: BusinessDTO;
    businessName: string;
    businessDomain: string;

    domains: Domain[] = [
        {value: 'Beauty'},
        {value: 'Healthcare'},
        {value: 'Retail'},
        {value: 'Other'}
      ];
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

    businessId: number;

    constructor(
        private http: HttpClient,
        private router: Router,
        private _formBuilder: FormBuilder,
        private userService: UserService,
        private serviceService: ServiceService,
        private googleAnalytics: GoogleAnalyticsService,
        private businessService: BusinessService
         ) { }

    ngOnInit() {
        this.firstFormGroup = this._formBuilder.group({
          firstCtrl: ['', Validators.required]
        });
        this.secondFormGroup = this._formBuilder.group({
          secondCtrl: ['', Validators.required]
        });
        this.thirdFormGroup = this._formBuilder.group({
            thirdCtrl: ['', Validators.required]
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

    getBusinessById(businessId: Number): BusinessDTO {
        this.businessService.getBusinessById(this.businessId).subscribe(
            res => {
                this.business = res;
            },
        );
        return this.business;
    }

    business_register() {
        // register business
        console.log(this.businessName);
        console.log(this.businessDomain);
        console.log(this.description);
        const payload_business: BusinessRegisterDTO = {
            name: this.businessName,
            domain: this.businessDomain,
            description: this.description
        };
        this.businessService.createBusiness(payload_business).subscribe(
            res => {
                console.log(res);
                this.businessId = res;

                const payload_service: ServiceCreateDto = {

                    name: this.service,
                    duration: this.service_duration,
                   // businessId: this.businessId
                     };
                 this.serviceService.registerService(this.businessId, payload_service).subscribe(
                     res => {
                        console.log(res);
                     },
                     err => console.log(err)
                 );
         // TODO: when register user, we need to add business id, also need to link service to the user
                 if (this.password === this.confirmPassword ) {
         console.log(this.firstName);
                     const payload: BusinessUserRegisterDTO = {

                        firstName: this.firstName,
                        lastName: this.lastName,
                        email: this.email,
                        password: this.password,
                        phoneNumber: null
                      //  businessId: this.businessId
                     };

                     if ( this.registerPhone) {
                         payload.phoneNumber = {
                            countryCode: this.selectedCountry['dialCode'],
                            areaCode: this.areaCode,
                            number: this.number

                         };
                     }

                     this.googleAnalytics.trackValues('formSubmit', 'register');

                     this.userService.businessRegister(this.businessId, payload).subscribe(
                         res => {
                             console.log(res);
                             this.router.navigate(['login']);
                         },
                         err => console.log(err)
                     );
                    } else {
                        alert('The passwords don\'t match.');
                    }
            },
            err => console.log(err)
        );

    }
    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }

    selectCountry(country: Object) {
        this.selectedCountry = country;
    }

}

/*
frontend for uploading
 <div fxLayout.gt-sm="row" fxLayout.lt-md="column" fxLayoutGap.gt-sm="20px">
            <p> Upload your business logo </p>
            <input type="file" (change) = "onFileSelected($event)" placeholder="Upload file" accept=".jpeg,.png">
            <button mat-button color="primary" type="button" (click)="onUpload()">Upload</button>

            </div>

            */