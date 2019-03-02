import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {GoogleAnalyticsService} from "../../services/google/google-analytics.service";
import {ThemeService} from "../../core/theme/theme.service";

@Component({
  selector: 'app-business-view',
  templateUrl: './business-view.component.html',
  styleUrls: ['./business-view.component.scss']
})
export class BusinessViewComponent implements OnInit {

    darkModeActive :boolean;
  constructor(private router: Router, public themeService: ThemeService) { }

  ngOnInit() {
      this.themeService.darkModeState.subscribe(value => {
          this.darkModeActive = value;
      })
  }

  getAllAppointmentsForUserForBusiness(businessId: number){
    //TODO CALL GET ALL APPOINTMENTS BY BUSINESS ID GOTTEN FROM CARD -- HAVE TO WAIT FOR GET ALL BUSINESS ROUTE FROM BACKEND
    //TEMP FIX TO FIX LAYOUT
    console.log("bye")
    this.router.navigate(['home']);
  }

  bookAppointmentForBusiness(businessId:number){
    //TODO GET BUSINESS APPOINTMENT FROM CARD AND CALL THIS FUNCTION -- HAVE TO WAIT FOR GET ALL BUSINESS ROUTE FROM BACKEND
  }

}
