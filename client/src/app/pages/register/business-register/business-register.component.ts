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
    fileSelectMsg: string = 'No file selected yet.';
    fileUploadMsg: string = 'No file uploaded yet.';
    disabled: boolean = false;
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

        const payload_service: ServiceCreateDto = {

          name: this.service,
          duration: this.service_duration,
           };
        
        let payload: BusinessUserRegisterDTO = null;
        if (this.password === this.confirmPassword ) {
           payload = {
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
        this.businessService.createBusiness(payload_business,payload_service,payload,this.selectedFile).subscribe(
            res => {
                console.log(res);
            },
            err => console.log(err)
        );

    }
  }
  
  togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }

  selectCountry(country: Object) {
        this.selectedCountry = country;
  }


  selectEvent(file: File): void {
    this.selectedFile = file;
    this.fileSelectMsg = file.name;
  }

  uploadEvent(file: File): void {
    this.fileUploadMsg = file.name;
  }

  cancelEvent(): void {
    this.fileSelectMsg = 'No file selected yet.';
    this.fileUploadMsg = 'No file uploaded yet.';
  }

  toggleDisabled(): void {
    this.disabled = !this.disabled;
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
