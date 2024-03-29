import { Component, Inject, Input } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Service } from "../../../../../models/service/Service";
import { ServiceService } from "../../../../../services/service/service.service";
import { UserToDisplay } from "../../../../../models/user/UserToDisplay";
import { UserService } from '../../../../../services/user/user.service';

export interface DialogData {
    user: UserToDisplay,
    services: Service[]
}

@Component({
    selector: 'assign-services-dialog',
    templateUrl: 'assign-services-dialog.component.html',
})
export class AssignServicesDialogComponent {
    selectedService: Service;

    constructor(
        public dialogRef: MatDialogRef<AssignServicesDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: DialogData,
        private serviceService: ServiceService,
        private userService: UserService) {}

    onNoClick(): void {
        this.dialogRef.close();
    }

    onDialogAccept(): void {

        this.serviceService.addServiceToUser(this.data.user.id, this.selectedService.id, this.userService.getBusinessIdFromToken()
        ).subscribe(
                res => this.dialogRef.close(),
                err => console.log(err)
            );
        }
}
