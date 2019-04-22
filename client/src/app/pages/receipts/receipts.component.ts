import {Component, OnInit} from '@angular/core';
import {BusinessService} from "../../services/business/business.service";
import {ServiceDTO} from "../../interfaces/service/service-dto";
import {ServiceService} from "../../services/service/service.service";
import * as html2pdf from 'html2pdf.js';
import {ActivatedRoute} from "@angular/router";
import {BusinessDTO} from "../../interfaces/business/business-dto";

@Component({
  selector: 'app-receipts',
  templateUrl: './receipts.component.html',
  styleUrls: ['./receipts.component.scss']
})
export class ReceiptsComponent implements OnInit {
    services: ServiceDTO[];
    business: BusinessDTO;
    firstName: string;
    lastName: string;
    serviceName: string;
    price: number;
    today: string;
    dueDate: string;
    businessId: number;
  constructor(private route: ActivatedRoute,  private businessService : BusinessService, private serviceService: ServiceService ) {
      this.services = [];
      this.setTodayAndDueDate(new Date())
  }

  ngOnInit() {
      //const businessId =+ this.route.snapshot.paramMap.get('businessId');
      const businessId = parseInt(this.route.snapshot.paramMap.get('businessId'));
      this.setBusinessById(businessId);
      this.getAllServiceForBusiness(businessId);
  }

  setBusinessById(businessId: number){
      this.businessService.getBusinessById(businessId).subscribe(
          res => {
              this.business = res;
          },
      );
  }

    getAllServiceForBusiness(businessId: number) {
        this.serviceService.getServices(businessId).subscribe(
            result =>{
                for(const service of result){
                    this.services.push(service)
                }
            }
        )
    }

    setService(service: ServiceDTO) {
      if (service != null) {
          this.serviceName = service.name
      }
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
