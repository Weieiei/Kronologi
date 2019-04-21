import { Component, OnInit } from '@angular/core';
import {BusinessService} from "../../services/business/business.service";
import {BusinessDTO} from "../../interfaces/business/business-dto";
import {ServiceDTO} from "../../interfaces/service/service-dto";
import {ServiceService} from "../../services/service/service.service";
import * as html2pdf from 'html2pdf.js';

@Component({
  selector: 'app-receipts',
  templateUrl: './receipts.component.html',
  styleUrls: ['./receipts.component.scss']
})
export class ReceiptsComponent implements OnInit {
    businesses: BusinessDTO[];
    services: ServiceDTO[];
    firstName: string;
    lastName: string;
    businessName: string;
    serviceName: string;
    price: number;
    today: string;
    dueDate: string;
  constructor(  private businessService : BusinessService, private serviceService: ServiceService ) {
      this.businesses =[];
      this.services = [];
      this.setTodayAndDueDate(new Date())
  }

  ngOnInit() {
      this.getAllBusiness()
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

    getAllServiceForBusiness(business: BusinessDTO) {
      this.businessName = business.name;
        this.serviceService.getServices(business.id).subscribe(
            result =>{
                for(const service of result){
                    this.services.push(service)
                }
            }
        )
    }

    setService(service: ServiceDTO) {
      this.serviceName = service.name
    }

    setDate(date: any){
      this.setTodayAndDueDate(date.value);
    }

    setTodayAndDueDate(date: Date) {
        this.today = date.toDateString();
        this.dueDate = new Date(date.setMonth(date.getMonth() + 1)).toDateString();

    }

    createPDF() {
        const doc = html2pdf();
        let invoice = document.getElementById('invoice');
        invoice.style.display = 'block';
        doc.from(invoice).save();

    }
}
