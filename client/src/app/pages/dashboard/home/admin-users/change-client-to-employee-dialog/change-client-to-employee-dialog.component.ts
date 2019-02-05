import { Component, Inject, Input } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserToDisplay } from "../../../../../models/user/UserToDisplay";
import { UserService } from "../../../../../services/user/user.service";

export interface DialogData {
    user: UserToDisplay,
}

@Component({
    selector: 'change-client-to-employee-dialog',
    templateUrl: 'change-client-to-employee-dialog.component.html',
})
export class ChangeClientToEmployeeDialogComponent {

    constructor(
        public dialogRef: MatDialogRef<ChangeClientToEmployeeDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: DialogData,
        private userService: UserService) {}

    onNoClick(): void {
        this.dialogRef.close();
    }

    onDialogAccept(employeeId: number): void {
        this.userService.changeUserToEmployee(this.data.user.id
        ).subscribe(
            res => this.dialogRef.close(),
            err => console.log(err)
        );
    }
}
