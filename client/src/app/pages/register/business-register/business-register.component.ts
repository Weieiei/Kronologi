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
import { BusinessHoursDTO } from '../../../interfaces/business/businessHours-dto'
import { map } from 'rxjs/operators';
export interface Domain {
    value: string;
  }

  export interface BusinessHours {
    start: string;
    end: string;
  }
@Component ({
  selector: 'app-business-register',
  templateUrl: './business-register.component.html',
  styleUrls: ['./business-register.component.scss']
})
export class BusinessRegisterComponent implements OnInit {

   daysOfWeek : string[] = ["Monday","Tuesday","Wednesday", "Thursday", "Friday","Saturday","Sunday"]
   possibleBusinessHours : string[] = ["00:00", "01:00", "02:00", "03:00", "04:00","05:00","06:00", "07:00",
                                        "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15,00",
                                        "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"]

    businessHourMap = new Map();
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

        let businessHoursDTO : BusinessHoursDTO[] = [];
        if(!this.isEmptyObject(this.businessHourMap)){
          console.log("hello")
          this.businessHourMap.forEach((openAndClose: BusinessHours, day: string) => {
             console.log(day)
             let businessHourDTOtemp : BusinessHoursDTO = {
                    day: day,
                    openHour : openAndClose.start,
                    closeHour : openAndClose.end
             }
             businessHoursDTO.push(businessHourDTOtemp);
          });
        }

        console.log(this.businessHourMap)
        console.log(businessHoursDTO);
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
            this.businessService.createBusiness(payload_business,payload_service,payload, businessHoursDTO,this.selectedFile).subscribe(
            res => {
               this.router.navigate(['login']);
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
  saveStartTime(day: string, time:string):void{
    if(this.businessHourMap.has(day)){
      let businessHours : BusinessHours = this.businessHourMap.get(day);
      businessHours.start = time;
    }else{
      let businessHours : BusinessHours = {start: time, end: ""};
      this.businessHourMap.set(day,businessHours);
    }
  }

  saveEndTime(day: string, time:string):void{
    if(this.businessHourMap.has(day)){
      let businessHours : BusinessHours = this.businessHourMap.get(day);
      businessHours.end = time;
    }else{
      let businessHours : BusinessHours = {start: "", end: time};
      this.businessHourMap.set(day,businessHours);
    }
  }
  isTimeLower(day:string, end: string): boolean{
    if(this.businessHourMap.has(day)){
      
      let startString : string[] = this.businessHourMap.get(day).start.split(':');
      let startTime : number = (+startString[0]) * 60 * 60 + (+startString[1]) * 60;

      let endString : string[] = end.split(':');
      let endTime : number = (+endString[0]) * 60 * 60 + (+endString[1]) * 60;

      if(endTime <= startTime){
        return true;
      }
    }
    return false;
  }

  isEmptyObject(obj: any): boolean {
    return Object.keys(obj).length === 0 && obj.constructor === Object;
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
