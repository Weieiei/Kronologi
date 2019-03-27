import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {GoogleAnalyticsService} from "../../services/google/google-analytics.service";
import {ThemeService} from "../../core/theme/theme.service";
import {MatDialog, MatDialogConfig} from "@angular/material";
import { BusinessDTO } from 'src/app/interfaces/business/business-dto';
import { BusinessService } from 'src/app/services/business/business.service';

@Component({
  selector: 'app-business-view',
  templateUrl: './business-view.component.html',
  styleUrls: ['./business-view.component.scss']
})
export class BusinessViewComponent implements OnInit {

    businesses: BusinessDTO[];
    darkModeActive :boolean;

  constructor(private router: Router,private dialog: MatDialog, public themeService: ThemeService, private businessService : BusinessService) { 
    this.businesses =[];
  }
  ngOnInit() {
      this.getAllBusiness();
      this.themeService.darkModeState.subscribe(value => {
          this.darkModeActive = value;
      })
      console.log("bussineses");
      console.log(this.businesses);
  }

  getAllAppointmentsForUserForBusiness(businessId: number){
    //TODO CALL GET ALL APPOINTMENTS BY BUSINESS ID GOTTEN FROM CARD -- HAVE TO WAIT FOR GET ALL BUSINESS ROUTE FROM BACKEND
    //TEMP FIX TO FIX LAYOUT
    this.router.navigate(['home']);
  }

  openModalForService(businessId:number){
    //TODO GET BUSINESS SERVICES FROM CARD AND CALL THIS FUNCTION -- HAVE TO WAIT FOR GET ALL BUSINESS ROUTE FROM BACKEND

    }

    getAllBusiness(): void{
        this.businessService.getAllBusinesses().subscribe(
          result =>{
            console.log(result)
            for(const business of result){
                this.businesses.push(business)
            }
          }
        )
    }
  }


