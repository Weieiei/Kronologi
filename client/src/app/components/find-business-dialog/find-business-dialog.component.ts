import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { BusinessService } from '../../services/business/business.service'
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import { BusinessDTO } from "../../interfaces/business/business-dto";
import { BusinessHoursDTO } from "../../interfaces/business/businessHours-dto";
import { trigger, state, style, transition, animate, group, query, stagger, keyframes } from '@angular/animations';
import { NgxSpinnerService } from 'ngx-spinner';
import { GeocodingApiService } from '../../services/google/geocode.service'


@Component({
  selector: 'app-find-business-dialog',
  templateUrl: './find-business-dialog.component.html',
  styleUrls: ['./find-business-dialog.component.scss'],
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
  ],
  styles: [`
  agm-map {
    height: 200px;
    width:  200px;
  }
`]
})
export class FindBusinessDialogComponent implements OnInit {
  chosenBusiness : BusinessDTO;
  mapType = 'roadmap';
  form: FormGroup;
  animationState : boolean = true;
  businessWasFetched: boolean = false;
  BusinessName : string
  businessArray : BusinessDTO[];
  daysOfWeek : string[] = ["Monday","Tuesday","Wednesday","Thursday", "Friday", "Saturday", "Sunday"];
  constructor(private geoCodeService : GeocodingApiService, private dialogRef: MatDialogRef<FindBusinessDialogComponent>, private spinner: NgxSpinnerService,private fb : FormBuilder,private businessService : BusinessService,  @Inject(MAT_DIALOG_DATA)public data:BusinessDTO) {
    this.businessArray = [];
  }
   

  ngOnInit() {
  }


  searchBusiness(businessName : string):void{
      this.spinner.show();
      this.animationState = false;
      this.businessService.findBusinessThroughGoogle(businessName).subscribe(
        res => {
            console.log(res)
            for(const business of res){
              this.updateLatLngFromAddress(business);
              if(business.business_hours.length < 7){
                for(const [index, value] of business.business_hours.entries()){
                  console.log(index);
                  console.log(value);
                  if(business.business_hours[index].day !== this.daysOfWeek[index]){
                    let businessHourTemp: BusinessHoursDTO = {
                        day : this.daysOfWeek[index],
                        openHour: "Close",
                        closeHour: "Close"
                    }
                    console.log(businessHourTemp);
                    business.business_hours.splice(index, 0, businessHourTemp);
                  }
                }
              }
              if(business.business_hours.length < 7){
                let businessHourTemp: BusinessHoursDTO = {
                  day : "Sunday",
                  openHour: "Close",
                  closeHour: "Close"
              }
                business.business_hours.push(businessHourTemp);
              }
              this.businessArray.push(business);
            }
            this.businessWasFetched = true;
            this.animationState = true;
            this.spinner.hide();
            this.dialogRef.updateSize("600px","600px")
            
        },
        err => console.log(err)
      );
  }

  updateLatLngFromAddress(business: BusinessDTO) {
    let businessArr : string[] = business.formattedAddress.split(',');
    let address = businessArr[0];
    let portalCode ="";
    let selectedPlace = "";
    this.geoCodeService
        .findFromAddress(address, null, null, null, null, null)
        .subscribe(response => {
            if (response.status == 'OK') {
                business.lat = response.results[0].geometry.location.lat;
                business.lng = response.results[0].geometry.location.lng;
            } else if (response.status == 'ZERO_RESULTS') {
                console.log('geocodingAPIService', 'ZERO_RESULTS', response.status);
            } else {
                console.log('geocodingAPIService', 'Other error', response.status);
            }
        });
  }

  onSelectionChange(business: BusinessDTO) : void{
    this.chosenBusiness = business;
    this.data = business;
  }

  closeDialog() : void {
    this.dialogRef.close();
  }
}
