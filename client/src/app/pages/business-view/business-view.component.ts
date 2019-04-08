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
  }


    getAllBusiness(): void{
        this.businessService.getAllBusinesses().subscribe(
          result =>{
            for(const business of result){
                this.businesses.push(business)
            }
          }
        )
    }
  }


