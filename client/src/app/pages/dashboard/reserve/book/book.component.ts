import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ServiceDTO} from "../../../../interfaces/service/service-dto";
import {MatDatepickerInputEvent} from "@angular/material";
import {AppointmentService} from "../../../../services/appointment/appointment.service";
import {EmployeeFreeTime} from "../../../../interfaces/employee/employee-free-time";

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})
export class BookComponent implements OnInit {
    firstFormGroup: FormGroup;
    secondFormGroup: FormGroup;
    isOptional = false;
    service: ServiceDTO;
    date: any;
    appointmentService: AppointmentService;
    employeeAvailabilitiesMap: Map <number, Array<EmployeeFreeTime>>;
    constructor(private _formBuilder: FormBuilder, private appointmentService: AppointmentService) {
    }

    ngOnInit() {
        this.firstFormGroup = this._formBuilder.group({
            firstCtrl: ''
        });
        this.secondFormGroup = this._formBuilder.group({
            secondCtrl: ''
        });
    }

    setService(service: ServiceDTO): void {
        this.service = service;
      //  this.getAllAvailabilitiesForService(this.service);
    }

    serviceIsSelected():boolean{
        return !!this.service;
    }

    setDate(date: any){
        this.date = date;
    }

    dateIsSelected():boolean{
        return !!this.date;
    }

    getAllAvailabilitiesForService(service: ServiceDTO){
        this.appointmentService.getAvailabilitiesForService(service.id).subscribe(
            res => {
                console.log(res);
                //organise into map
                var availabilities;
                for (let eachEmployee of res){
                        for (let key in eachEmployee){
                            if (key == 'availabilities'){
                                //TODO figure out how to get the availabilities from here
				console.log('hi');
                                console.log(eachEmployee[key]);
                                console.log('hi');
                                availabilities.add(eachEmployee[key]);
                            }
                        }


                }
            });
    }
}
