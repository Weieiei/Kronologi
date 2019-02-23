import { Component, OnInit } from "@angular/core";
import { ServiceService } from "../../services/service/service.service";
import {ServiceCreateDto} from "../../interfaces/service/service-create-dto";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
    selector: 'app-admin-create-service',
    templateUrl: './create-service.component.html',
    styleUrls: ['./create-service.component.scss']
})
export class CreateServiceComponent implements OnInit {

    name: string;
    duration: number;
    serviceForm: FormGroup;

    constructor(
        private serviceService: ServiceService,
        private fb: FormBuilder,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.initForm();
    }

    initForm(): void {
        this.serviceForm = this.fb.group({
            name: [this.name, [ Validators.required ]],
            duration: [this.duration, [ Validators.required ]],
        });
    }

    createService() {
        const serviceCreateDTO: ServiceCreateDto = {
            id: 0,
            name: this.serviceForm.value.name,
            duration: this.serviceForm.value.duration
        };
        this.serviceService.createService(serviceCreateDTO).subscribe(
            res => this.router.navigate(['admin/services']),
            err => console.log(err)
        );
    }
}
