import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {MatDialog, MatDialogConfig} from "@angular/material";
@Component({
  selector: 'app-business-view',
  templateUrl: './business-view.component.html',
  styleUrls: ['./business-view.component.scss']
})
export class BusinessViewComponent implements OnInit {

  constructor(private router: Router,private dialog: MatDialog) { }

  ngOnInit() {
  }

  getAllAppointmentsForUserForBusiness(businessId: number){
    //TODO CALL GET ALL APPOINTMENTS BY BUSINESS ID GOTTEN FROM CARD -- HAVE TO WAIT FOR GET ALL BUSINESS ROUTE FROM BACKEND
    //TEMP FIX TO FIX LAYOUT
    this.router.navigate(['home']);
  }

  openModalForService(businessId:number){
    //TODO GET BUSINESS SERVICES FROM CARD AND CALL THIS FUNCTION -- HAVE TO WAIT FOR GET ALL BUSINESS ROUTE FROM BACKEND

    }
  }


