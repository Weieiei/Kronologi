import {Component, OnInit} from "@angular/core";
import {ServiceService} from "../../services/service/service.service";
import {ServiceCreateDto} from "../../interfaces/service/service-create-dto";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialogConfig} from "@angular/material";

@Component({
    selector: 'app-admin-create-service',
    templateUrl: './create-service.component.html',
    styleUrls: ['./create-service.component.scss']
})
export class CreateServiceComponent implements OnInit {

    name: string;
    duration: number;
    businessId: number;
    serviceForm: FormGroup;

    //  const dialogConfig = new MatDialogConfig();

    constructor(
        private serviceService: ServiceService,
        private fb: FormBuilder,
        private router: Router,
        private route: ActivatedRoute,

    ) {
    }

    ngOnInit() {
        this.initForm();
        this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"));

    }

    initForm(): void {
        this.serviceForm = this.fb.group({
            name: [this.name, [ Validators.required ]],
            duration: [this.duration, [ Validators.required ]],
        });
    }

    createService() {
        const serviceCreateDTO: ServiceCreateDto = {
            name: this.serviceForm.value.name,
            duration: this.serviceForm.value.duration,
        };
        this.serviceService.createService(this.businessId, serviceCreateDTO).subscribe(
            res => this.router.navigate(['admin/services']),
            err => console.log(err)
        );
    }

    openDialog(errorMessage: any) {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = {
            title: "ERROR",
            message: errorMessage,
        };
        // this.dialog.open(ErrorDialogComponent, dialogConfig);
    }


}
