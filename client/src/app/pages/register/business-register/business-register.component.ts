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
import { HttpErrorResponse } from '@angular/common/http';
import { TdFileUploadComponent } from '@covalent/core/file';
//npm i -save @covalent/core to  install

export interface Domain {
    value: string;
  }
@Component ({
  selector: 'app-business-register',
  templateUrl: './business-register.component.html',
  styleUrls: ['./business-register.component.scss']
})
export class BusinessRegisterComponent implements OnInit {
 // Fields to upload a profiel picture
 selectedFile: File = null;
 fileSelectMsg: string = 'No file selected yet.';
 fileUploadMsg: string = 'No file uploaded yet.';
 disabled: boolean = false;
 //formGroup
    firstFormGroup: FormGroup;
    secondFormGroup: FormGroup;
    thirdFormGroup: FormGroup;

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
    serviceDuration: number;

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

        this.thirdFormGroup = this._formBuilder.group({
           thirdCtrl: ['', Validators.required],
            newServices: this._formBuilder.array([])
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

    businessRegister(): void {
        // register business

        if (this.password === this.confirmPassword ) {
        console.log(this.newServiceForms.length);
        console.log(this.newServiceForms);
        console.log(this.serviceDuration);
        console.log(this.serviceDuration + 'service duration');
        console.log('service duration');
        const payloadBusiness: BusinessRegisterDTO = {
            name: this.businessName,
            domain: this.businessDomain,
            description: this.description
        };

        this.businessService.createBusiness(payloadBusiness).subscribe(
            res => {
                console.log(res);
                this.businessId = res;
                const payloadService: ServiceCreateDto = {
                    name: this.service,
                    duration: this.serviceDuration
                };
                console.log(payloadService);

                console.log(this.businessId);
                this.serviceService.registerService(this.businessId, payloadService, this.selectedFile ).subscribe(
                     res => {
                        console.log(res);
                     },
                     err => console.log(err)
                 );

            console.log(this.newServiceForms.length + 'length');
            for( var _i = 0; _i < this.newServiceForms.length; _i++) {
                console.log(this.newServiceForms.at(_i).value.newServiceName);

                console.log(this.newServiceForms.at(_i).value);
                const payloadNewService: ServiceCreateDto = {

                    name: this.newServiceForms.at(_i).value.newServiceName,
                    duration: this.newServiceForms.at(_i).value.newServiceDuration
                };
                this.serviceService.registerService(this.businessId, payloadNewService, this.selectedFile).subscribe(
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
        return this.thirdFormGroup.get('newServices') as FormArray;
      }

    addService() {

        const newService = this._formBuilder.group({
          newServiceName: [],
          newServiceDuration: [],
    });
    this.newServiceForms.push(newService);
}
deleteService(i) {
    this.newServiceForms.removeAt(i);
  }

  selectEvent(file: File): void {
    this.selectedFile = file;
    console.log(this.selectedFile);
    this.fileSelectMsg = file.name;
  }

   uploadEvent(file: File): void {
    this.fileUploadMsg = file.name;
  }

   cancelEvent(): void {
    this.fileSelectMsg = 'No file selected yet.';
    this.fileUploadMsg = 'No file uploaded yet.';
  }

  addServicePicture(): void {
    console.log(this.selectedFile);
  if (this.selectedFile != null) {
      this.userService.uploadUserPicture(this.selectedFile).subscribe(
          res => {

              console.log('File seccessfully uploaded. ');

          },
          err => {
              if (err instanceof HttpErrorResponse) {
                  err => console.log(err);
              }
          }
      );
  }
}

}


