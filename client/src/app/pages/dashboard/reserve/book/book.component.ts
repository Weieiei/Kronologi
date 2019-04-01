import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ServiceDTO} from "../../../../interfaces/service/service-dto";
import {MatDatepickerInputEvent} from "@angular/material";

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
    constructor(private _formBuilder: FormBuilder) {}

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
}
