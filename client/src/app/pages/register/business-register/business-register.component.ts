import { Component, OnInit,  ViewChild, Renderer2, ViewChildren, ElementRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ServiceService } from '../../../services/service/service.service';
import { BusinessService } from '../../../services/business/business.service';
import { BusinessUserRegisterDTO } from '../../../interfaces/user/business-user-register-dto';
import { BusinessRegisterDTO } from '../../../interfaces/business/business-register-dto';
import { BusinessDTO } from '../../../interfaces/business/business-dto';
import { UserService } from '../../../services/user/user.service';
import { FormBuilder, FormGroup, Validators, FormArray , FormControl} from '@angular/forms';
import * as countryData from 'country-telephone-data';
import { GoogleAnalyticsService } from 'src/app/services/google/google-analytics.service';
import { ServiceCreateDto } from '../../../interfaces/service/service-create-dto';
import { BusinessHoursDTO } from '../../../interfaces/business/businessHours-dto'
import { FindBusinessDialogComponent } from '../../../components/find-business-dialog/find-business-dialog.component';
import { MatDialogConfig, MatDialog } from '@angular/material';
import { NgxSpinnerService } from 'ngx-spinner';
import { trigger, state, style, transition, animate, group } from '@angular/animations';
import { PasswordMismatchStateMatcher } from '../../../../shared/password-mismatch-state-matcher';
import { TermsAndServicesDialogComponent } from 'src/app/components/terms-and-services-dialog/terms-and-services-dialog.component';
import { StripeUserInfo } from 'src/app/interfaces/user/stripe-user-info';
import { StripeScriptTag, StripeToken , StripeSource, StripeInstance} from "stripe-angular"
import { BankAccountInfo } from 'src/app/interfaces/user/seller-bank-account-dto';
import { RoutingAndAccountHelpComponent } from 'src/app/components/routing-and-account-help/routing-and-account-help.component';


export interface Domain {
    value: string;
}

export interface Provinces{
    value: string;
}
export interface AccountType{
    value: string
}
export interface BusinessHours {
    start: string;
    end: string;
}

@Component ({
  selector: 'app-business-register',
  templateUrl: './business-register.component.html',
  styleUrls: ['./business-register.component.scss'],
  animations: [
    trigger('slideInOut', [
      state('true', style({
          'max-height': '500px', 'opacity': '1', 'visibility': 'visible'
      })),
      state('false', style({
          'max-height': '0px', 'opacity': '0', 'visibility': 'hidden'
      })),
      transition('true => false', [group([
          animate('1ms ease-in-out', style({
              'opacity': '0'
          })),
          animate('1ms ease-in-out', style({
              'max-height': '0px'
          })),
          animate('1ms ease-in-out', style({
              'visibility': 'hidden'
          }))
      ]
      )]),
      transition('false => true', [group([
          animate('1ms ease-in-out', style({
              'visibility': 'visible'
          })),
          animate('1ms ease-in-out', style({
              'max-height': '500px'
          })),
          animate('1ms ease-in-out', style({
              'opacity': '1'
          }))
      ]
      )])
    ])
  ]
})
export class BusinessRegisterComponent implements OnInit {
     @ViewChildren('start') start;
    @ViewChild('end') end;
    private publishableKey:string ="pk_test_Abn7eEcmv3zmYE39xC2PGovO00rTzkGohi";
    loaded:boolean
    lastError:Error
    token:any
    stripe:StripeInstance
    checked: boolean = false;
    termOfServices: boolean = false;
    tosDateAcceptance:Date;
    daysOfWeek: string[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    possibleBusinessHours: string[] = ['00:00', '00:30', '01:00', '01:30', '02:00', '02:30', '03:00', '03:30', '04:00', '04:30', '05:00',
        '05:30', '06:00', '06:30', '07:00', '07:30', '08:00', '08:30', '09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '12:00',
        '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00',
        '19:30', '20:00', '20:30', '21:00', '21:30', '22:00', '22:30', '23:00', '23:30', '24:00'];
    selectedBusiness: BusinessDTO;
    businessHourMap = new Map();
    animationState = true;
    fileSelectMsg = 'No file selected yet.';
    fileUploadMsg = 'No file uploaded yet.';
    disabled = false;
    personalInfoForm: FormGroup;
    businessInfoForm: FormGroup;
    serviceInfoForm: FormGroup;
    extraInformation: FormGroup;
    selectedFile: File = null;
    // new business object
    business: BusinessDTO;
    businessName: string;
    businessDomain: string;

    bankAccountType: AccountType[] = [
        {value: 'Individual'},
        {value: 'Business'}
    ];


    provinceChoice: Provinces[] = [
        {value: 'QC'},
        {value: 'ON'},
        {value: 'AB'},
        {value: 'BC'},
        {value: 'NB'},
        {value: 'NL'},
        {value: 'NS'},
        {value: 'NT'},
        {value: 'NU'},
        {value: 'PE'},
        {value: 'SK'},
        {value: 'YT'}
    ];
    domains: Domain[] = [
        {value: 'Beauty'},
        {value: 'Healthcare'},
        {value: 'Retail'},
        {value: 'Other'}
    ];
    description: string;
    address: string;
    city:  string;
    province: string;
    country: string;
    postalCode: string;
    // new service object
    service: string;
    serviceDuration: number;
    servicePrice: number;
    // new user object
    firstName: string;
    lastName: string;
    email: string;
    password: string;

    dateOfBirth: Date;
    socialSecurityNumber:number;
    businessTaxId: number; 
    countries: Object[] = countryData.allCountries;
    selectedCountry: Object;

    bankRoutingNumber: number;
    bankAccountNumber: number;
    bankAccountHolderFn: string;
    bankAccountHolderLn: string;
    accountType: string;

    areaCode: string;
    number: string;

    confirmPassword: string;
    isPasswordVisible = false;

    registerPhone = false;

    businessId: number;
    matcher: PasswordMismatchStateMatcher;

    stripeTokenForBankAccount : StripeToken;

    index = 0;
    constructor(
        private spinner: NgxSpinnerService,
        private dialog: MatDialog,
        private http: HttpClient,
        private router: Router,
        private _formBuilder: FormBuilder,
        private userService: UserService,
        private serviceService: ServiceService,
        private googleAnalytics: GoogleAnalyticsService,
        private businessService: BusinessService,
        public StripeScriptTag:StripeScriptTag,
        public elRef : ElementRef
         ) {
        this.matcher = new PasswordMismatchStateMatcher();
    }

    ngOnInit() {
        this.apply(this.publishableKey).then( ()=>this.loaded=true )
        this.personalInfoForm = this._formBuilder.group({
            firstName: [this.firstName || '',  [Validators.required] ],
            lastName: [this.lastName || '',  [Validators.required] ],
            email: [this.email || '',  [Validators.required, Validators.email] ],
            password: [this.password || '',  [Validators.required, Validators.pattern('^(?=.*\\d)(?=.*[a-zA-Z]).{6,30}$')]],
            confirmPassword: [this.confirmPassword || '',  [Validators.required]],
            areaCode: [this.areaCode || '',  [] ],
            number: [this.number || '',  [] ],
        }, {validator: this.checkPasswords});
        this.businessInfoForm = this._formBuilder.group({
            businessDomain: [this.businessDomain || '',  [Validators.required] ],
            businessName: [this.businessName || '',  [Validators.required] ],
            description: [this.description || '',  [Validators.required] ],
            address: [this.address || '',  [Validators.required] ],
            city: [this.city || '',  [Validators.required] ],
            province: [this.province || '',  [Validators.required] ],
            country: [this.country || '',  [Validators.required] ],
            postalCode: [this.postalCode || '',  [Validators.required] ],

        });
        this.serviceInfoForm = this._formBuilder.group({
            firstNewService: [this.service, [Validators.required]],
            firstNewServiceDuration: [this.serviceDuration, [Validators.required]],
            firstNewServicePrice: [this.servicePrice, [Validators.required]],
            newServices: this._formBuilder.array([])
        });

        this.extraInformation = this._formBuilder.group({
            dateOfBirth: new FormControl(new Date()),
            socialSecurityNumber: [this.socialSecurityNumber || '', [Validators.required]],
            businessTaxId: [this.businessTaxId],
            bankRoutingNumber: [this.bankRoutingNumber || '', [Validators.required, Validators.pattern('(([0-9]){5}[-]([0-9]){3})|([0-9]){8}')]],
            bankAccountNumber: [this.bankAccountNumber || '', [Validators.required, Validators.pattern('[0-9]{7,12}')]],
            bankAccountHolderFn: [this.bankAccountHolderFn || '', [Validators.required]],
            bankAccountHolderLn: [this.bankAccountHolderLn || '', [Validators.required]],
            accountType: [this.accountType || '', [Validators.required]],
            provinceChoice: new FormControl()        
        });
        
      }

    apply(key):Promise<StripeInstance>{
     this.publishableKey = key
        return this.StripeScriptTag.setPublishableKey(this.publishableKey)
    .   then(StripeInstance=>this.stripe=StripeInstance)
    }
    checkPasswords(inputFormGroup: FormGroup) {
        const password = inputFormGroup.controls.password.value;
        const confirmPassword = inputFormGroup.controls.confirmPassword.value;
        return password === confirmPassword ? null : { mismatched: true };
    }


    getBusinessById(businessId: Number): BusinessDTO {
        this.businessService.getBusinessById(this.businessId).subscribe(
            res => {
                this.business = res;
            },
        );
        return this.business;
    }

    setStripeToken( token:StripeToken ){
        this.stripeTokenForBankAccount = token;
      }
    business_register() {
        this.spinner.show();
        this.animationState = false;
        const businessHoursDTO: BusinessHoursDTO[] = [];
        if (!this.isEmptyObject(this.businessHourMap)) {
            this.businessHourMap.forEach((openAndClose: BusinessHours, day: string) => {
                const businessHourDTOTemp: BusinessHoursDTO = {
                    day: day,
                    openHour : openAndClose.start,
                    closeHour : openAndClose.end
                };
                businessHoursDTO.push(businessHourDTOTemp);
            });
        }

        const businessInfoValues = this.businessInfoForm.value;
        const finalizedAddress: string = businessInfoValues.address + ',' + businessInfoValues.city + ',' + businessInfoValues.province
            + ' ' + businessInfoValues.postalCode;
        const payload_business: BusinessRegisterDTO = {
            name: businessInfoValues.businessName,
            domain: businessInfoValues.businessDomain,
            description: businessInfoValues.description,
            formatted_address: finalizedAddress
        };

        const serviceInfoValues = this.serviceInfoForm.value;
        const firstService = {
            name: serviceInfoValues.firstNewService,
            duration: serviceInfoValues.firstNewServiceDuration,
            price: serviceInfoValues.firstNewServicePrice
        };

        const payload_service: ServiceCreateDto[] = [firstService];

        if (this.newServiceForms) {
            this.newServiceForms.controls.forEach((individualControl: FormGroup) => {
                const subsequentService = {
                    name: individualControl.controls.newServiceName.value,
                    duration: individualControl.controls.newServiceDuration.value,
                    price: serviceInfoValues.firstNewServicePrice
                };
                payload_service.push(subsequentService);
            });
        }

        const personalInfoValues = this.personalInfoForm.value;
        let payload: BusinessUserRegisterDTO = null;
        if (this.password === this.confirmPassword ) {
        payload = {
            firstName: personalInfoValues.firstName,
            lastName: personalInfoValues.lastName,
            email: personalInfoValues.email,
            password: personalInfoValues.password,
            phoneNumber: {
                countryCode: "+1",
                areaCode: personalInfoValues.areaCode,
                number: personalInfoValues.number
            }
        };
        
        const extraInfoStripe = this.extraInformation.value;
        console.log(extraInfoStripe)
        let bankAccountInformation: BankAccountInfo = null;
        let stripeSellerPayload: StripeUserInfo = null;

        if(this.checked){
            const birthYear = extraInfoStripe.dateOfBirth.getFullYear();
            const birthMonth = extraInfoStripe.dateOfBirth.getMonth();
            const birthDay = extraInfoStripe.dateOfBirth.getDate();
            
            stripeSellerPayload ={
                firstName: personalInfoValues.firstName,
                lastName: personalInfoValues.lastName,
                birthYear: birthYear,
                birthMonth: birthMonth,
                birthDay: birthDay,
                socialInsuranceNumber: extraInfoStripe.socialSecurityNumber,
                
                business_type: businessInfoValues.businessDomain,
                address: businessInfoValues.address,
                city:businessInfoValues.city,
                province:extraInfoStripe.provinceChoice,
                postalCode: businessInfoValues.postalCode,
                country:businessInfoValues.country,
                businessTaxNumber:extraInfoStripe.businessTaxId,

                tosAcceptance: this.tosDateAcceptance,
                stripeTokenForBankAccount :this.stripeTokenForBankAccount
            }

            bankAccountInformation = {
                routinNumber: extraInfoStripe.bankRoutingNumber,
                accountNumber: extraInfoStripe.bankAccountNumber,
                bankAccountHolderFirstName: extraInfoStripe.bankAccountHolderFn,
                bankAccountHolderLastName: extraInfoStripe.bankAccountHolderLn,
                accountType: extraInfoStripe.accountType
            }
        }
        this.googleAnalytics.trackValues('formSubmit', 'register');
        this.businessService.createBusiness(payload_business, payload_service, payload, businessHoursDTO, this.selectedFile, stripeSellerPayload,bankAccountInformation).subscribe(
        res => {
                this.router.navigate(['login']);
            },
            err => {
                    console.log(err);
                }
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

    saveStartTime(day: string, time: string): void {
        if  (this.businessHourMap.has(day)) {
            const businessHours: BusinessHours = this.businessHourMap.get(day);
        businessHours.start = time;
        } else {
            const businessHours: BusinessHours = {start: time, end: ''};
        this.businessHourMap.set(day, businessHours);
        }
    }

    saveEndTime(day: string, time: string): void {
        if (this.businessHourMap.has(day)) {
            const businessHours: BusinessHours = this.businessHourMap.get(day);
            businessHours.end = time;
        } else {
            const businessHours: BusinessHours = {start: '', end: time};
            this.businessHourMap.set(day, businessHours);
        }
    }

    isTimeLower(day: string, end: string): boolean {
        if (this.businessHourMap.has(day)) {
            const startString: string[] = this.businessHourMap.get(day).start.split(':');
            const startTime: number = (+startString[0]) * 60 * 60 + (+startString[1]) * 60;

            const endString: string[] = end.split(':');
            const endTime: number = (+endString[0]) * 60 * 60 + (+endString[1]) * 60;

            if (endTime <= startTime) {
                return true;
            }
        }
        return false;
    }

    isEmptyObject(obj: any): boolean {
        return Object.keys(obj).length === 0 && obj.constructor === Object;
    }

    openFindBusinessDialog() {
        const dialogConfig = new MatDialogConfig();

        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.width = '400px';
        dialogConfig.height = '400px';

        dialogConfig.data = {
            business: this.selectedBusiness
        };
        const dialogRef = this.dialog.open(FindBusinessDialogComponent, dialogConfig).afterClosed().subscribe(
            response => {
                if(response!== undefined){

                    this.businessInfoForm.controls["businessName"].setValue(response["name"])

                    let description : string = response["description"].replace("[","");
                    description = description.replace("]","");
                    description = description.replace("\"","");
                    this.businessInfoForm.controls["description"].setValue(description);
                    
                    let formattedAddressFound : string[] = response["formattedAddress"].split(",");

                    this.businessInfoForm.controls["address"].setValue(formattedAddressFound[0]);
                    this.businessInfoForm.controls["city"].setValue(formattedAddressFound[1]);
                    
                    let provinceAndPostalCode : string[] = formattedAddressFound[2].split(" ");
                    this.businessInfoForm.controls["province"].setValue(provinceAndPostalCode[1]);
                   
                    let postalCode = provinceAndPostalCode[2]+" "+provinceAndPostalCode[3];
                    this.businessInfoForm.controls["postalCode"].setValue(postalCode);

                    this.businessInfoForm.controls["country"].setValue(formattedAddressFound[3]);
                    
                   for(const child of this.start._results){
                       console.log(child)
                       if(child["controlType"] !== undefined){
                        console.log(child.trigger["nativeElement"].part.contains("value"))
                        
                       }
                   }
                }   
               
        });;

     
    }


    openTermsAndServicesDialog() {
        const dialogConfig = new MatDialogConfig();

        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.width = '600px';
        dialogConfig.height = '350px';

        this.dialog.open(TermsAndServicesDialogComponent, dialogConfig);

    }
    openRoutingAndAccountNumberHelp() {
        const dialogConfig = new MatDialogConfig();

        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.width = '700px';
        dialogConfig.height = '500px';

        this.dialog.open(RoutingAndAccountHelpComponent, dialogConfig);

    }

    stopRegistering() {
        this.animationState = true;
        this.spinner.hide();
    }

    get newServiceForms() {
        return this.serviceInfoForm.get('newServices') as FormArray;
    }
    addService() {
        const newService = this._formBuilder.group({
          newServiceName: ['', [Validators.required]],
          newServiceDuration: ['', [Validators.required]],
          newServicePrice: ['',[Validators.required]]
        });
        this.newServiceForms.push(newService);
    }
    deleteService(i) {
        this.newServiceForms.removeAt(i);
    }

    TOSDate():void{
        this.tosDateAcceptance = new Date();
    }
    checkForExtraInfo():boolean{
        if(this.checked){
            const extraInfo = this.extraInformation.value;
            const dob  = extraInfo.dateOfBirth;
            const  socialSecurityNumber = extraInfo.socialSecurityNumber
            if(dob === null || socialSecurityNumber === null || !this.termOfServices){
                return true;
            }
            return false;
        }
        return false;
    }
}

