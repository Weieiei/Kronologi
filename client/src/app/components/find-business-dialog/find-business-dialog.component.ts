import { Component, OnInit, Inject } from '@angular/core';
import { BusinessService } from '../../services/business/business.service'
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
  selector: 'app-find-business-dialog',
  templateUrl: './find-business-dialog.component.html',
  styleUrls: ['./find-business-dialog.component.scss']
})
export class FindBusinessDialogComponent implements OnInit {
  form: FormGroup;
  BusinessName : string
  
  constructor(private fb : FormBuilder,private businessService : BusinessService,  @Inject(MAT_DIALOG_DATA)public data:any) {}
   

  ngOnInit() {
  }


  searchBusiness(businessName : string):void{
      this.businessService.findBusinessThroughGoogle(businessName).subscribe(
        res => {
           console.log(res);
        },
        err => console.log(err)
      );
  }
}
