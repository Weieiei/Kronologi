import {Component, OnInit} from "@angular/core";
import {ServiceService} from "../../services/service/service.service";
import {ServiceCreateDto} from "../../interfaces/service/service-create-dto";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {MatDialog, MatDialogConfig} from "@angular/material";
import {ErrorDialogComponent} from "../../components/error-dialog/error-dialog.component";

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

    constructor(
        private dialog: MatDialog,
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
            price: this.serviceForm.value.price
            //TODO: need to get Admin' business
           // businessId: 0
        };
        this.serviceService.createService(this.businessId, serviceCreateDTO).subscribe(
            res => this.router.navigate([this.businessId,"admin","services"]),

            err => {
                this.openDialog(err["status"]);
            }
        )
    }

    openDialog(errorMessage: any) {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = {
            title: "ERROR",
            messageOrStatus: errorMessage,
        };
         this.dialog.open(ErrorDialogComponent, dialogConfig);
    }


    redirectToServices() {
        this.router.navigate([this.businessId + '/admin/services']);

    }
}
