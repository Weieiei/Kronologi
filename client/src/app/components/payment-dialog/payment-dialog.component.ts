import {  Component,
  AfterViewInit,
  OnDestroy,
  ViewChild,
  ElementRef,
  ChangeDetectorRef, 
  Inject} from '@angular/core';

import { StripeScriptTag, StripeToken , StripeSource, StripeInstance} from "stripe-angular"
import { NgForm } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { PaymentInfoDTO } from 'src/app/interfaces/business/payment-processing-dto';
import { ServiceDTO } from 'src/app/interfaces/service/service-dto';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
@Component({
  selector: 'app-payment-dialog',
  templateUrl: './payment-dialog.component.html',
  styleUrls: ['./payment-dialog.component.scss']
})
export class PaymentDialogComponent implements AfterViewInit, OnDestroy {
  @ViewChild('cardInfo') cardInfo: ElementRef;

  private publishableKey:string ="pk_test_Abn7eEcmv3zmYE39xC2PGovO00rTzkGohi";
  loaded:boolean
  lastError:Error
  token:any
  stripe:StripeInstance
  service: ServiceDTO
  businessId : number


  constructor(private appointmentService: AppointmentService,public StripeScriptTag:StripeScriptTag,private cd: ChangeDetectorRef, private dialogRef: MatDialogRef<PaymentDialogComponent>, @Inject(MAT_DIALOG_DATA) {service, businessId}:any){
    this.service = service;
    this.businessId = businessId
    this.apply(this.publishableKey).then( ()=>this.loaded=true )
    console.log(service)
    console.log(businessId)
  }

  ngAfterViewInit() {
  
  }

  ngOnDestroy() {;
  }

  apply(key):Promise<StripeInstance>{
    this.publishableKey = key
    return this.StripeScriptTag.setPublishableKey(this.publishableKey)
    .then(StripeInstance=>this.stripe=StripeInstance)
  }
  onStripeInvalid( error:Error ){
    console.log('Validation Error', error)
  }

  setStripeToken( token:StripeToken ){
  
    let paymentInfo : PaymentInfoDTO = {
      stripeToken: token,
      servicePrice: 100,
      businessId: this.businessId
    }

    this.appointmentService.payForAppointment(paymentInfo, this.businessId).subscribe(
      res => console.log(res)
    );
  }

  setStripeSource( source: StripeSource ){
    console.log('Stripe source', source)
  }

  onStripeError( error:Error ){
    console.error('Stripe error', error)
  }
}