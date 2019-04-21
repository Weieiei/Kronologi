import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {SnackBar} from "../../snackbar";

@Component({
  selector: 'app-error-dialog',
  templateUrl: './error-dialog.component.html',
  styleUrls: ['./error-dialog.component.scss']
})

export class ErrorDialogComponent implements OnInit {
    serviceName:string;
    isEmployee:boolean;
    isAdmin:boolean;
    aTitle:string;
    aMessage:string;

    constructor(
        private dialogRef: MatDialogRef<ErrorDialogComponent>,
        private snackBar: SnackBar,
        @Inject(MAT_DIALOG_DATA) {title,message}:any) {
        this.aTitle = title;
        this.aMessage = message;
    }

    ngOnInit() {
    }

    close() {
        this.dialogRef.close();
    }
}
