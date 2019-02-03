import { Component, OnInit } from "@angular/core";
import { ServiceService } from "../../services/service/service.service";
import {ServiceCreateDto} from "../../interfaces/service/service-create-dto";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-admin-create-service',
    templateUrl: './create-service.component.html',
    styleUrls: ['./create-service.component.scss']
})
export class CreateServiceComponent implements OnInit {

    name: string;
    duration: number;
    serviceForm: FormGroup;

    possibleDurations: number[] = [
        30,
        45,
        60,
        120,
        150,
        180
    ];

    constructor(
        private serviceService: ServiceService,
        private fb: FormBuilder
    ) {
    }

    ngOnInit() {
        this.initForm();
        console.log('Hi');
    }

    initForm(): void {
        this.serviceForm = this.fb.group({
            name: [this.name, [ Validators.required ]],
            duration: [this.duration, [ Validators.required ]],
        });
    }

    createService() {
        console.log('Called');
        const serviceCreateDTO: ServiceCreateDto = {
            id: 0,
            name: this.serviceForm.value.name,
            duration: this.serviceForm.value.duration
        };
        this.serviceService.createService(serviceCreateDTO).subscribe(
            res => console.log(res),
            err => console.log(err)
        );
    }
}
