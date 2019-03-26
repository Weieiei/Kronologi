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
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
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
    index: number = 0;
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
        // this.thirdFormGroup = this._formBuilder.group({
        //     thirdCtrl: ['', Validators.required]
        //   });
        this.thirdFormGroup = this._formBuilder.group({
           // service: '',
           // service_duration: '',
           thirdCtrl: ['', Validators.required],
            new_services: this._formBuilder.array([])
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

    business_register(): void {
        // register business

        if (this.password === this.confirmPassword ) {
        console.log(this.newServiceForms.length);
        console.log(this.newServiceForms);
        console.log(this.service_duration);
        console.log(this.service_duration +'service duration');
        console.log('service duration');
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
                    duration: this.service_duration
                };
                console.log(payload_service);

                console.log(this.businessId);
                this.serviceService.registerService(this.businessId, payload_service).subscribe(
                     res => {
                        console.log(res);
                     },
                     err => console.log(err)
                 );

            console.log(this.newServiceForms.length + 'length');
            for( var _i = 0; _i < this.newServiceForms.length; _i++) {
                console.log(this.newServiceForms.at(_i).value.new_service_name);

                console.log(this.newServiceForms.at(_i).value);
                const payload_new_service: ServiceCreateDto = {

                    name: this.newServiceForms.at(_i).value.new_service_name,
                    duration: this.newServiceForms.at(_i).value.new_service_duration
                };
                this.serviceService.registerService(this.businessId, payload_new_service).subscribe(
                    res => {
                     console.log(res);
                 },
                    err => console.log(err)
                );


            }

            
                     const payload: BusinessUserRegisterDTO = {

                        firstName: this.firstName,
                        lastName: this.lastName,
                        email: this.email,
                        password: this.password,
                        phoneNumber: null
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

    get newServiceForms() {
        return this.thirdFormGroup.get('new_services') as FormArray;
      }

    addService() {

        const new_service = this._formBuilder.group({
          new_service_name: [],
          new_service_duration: [],
    });
    this.newServiceForms.push(new_service);
}
deleteService(i) {
    this.newServiceForms.removeAt(i);
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
