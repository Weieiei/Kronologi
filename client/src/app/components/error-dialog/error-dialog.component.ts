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
    finalMessage:string;
    errorStatus: Map <number, string>;


    constructor(
        private dialogRef: MatDialogRef<ErrorDialogComponent>,
        private snackBar: SnackBar,
        @Inject(MAT_DIALOG_DATA) {title,messageOrStatus}:any) {
        this.aTitle = title;
        this.aMessage = messageOrStatus;
        this.finalMessage="";

        this.errorStatus = new Map<number ,string >();
        this.errorStatus.set(400, "Your input is incorrect, try again");
        this.errorStatus.set(500, "Woops, looks like this one's on us");
        this.errorStatus.set(300, "We moved this somewhere, if you can’t find what you’re looking for, try emailing" +
            " us");
        this.errorStatus.set(0, "Something went wrong, please try again later");
        this.errorStatus.set(403, "Something went wrong, please try again");




        if (this.errorStatus.has(parseInt(messageOrStatus)))
            this.finalMessage = this.errorStatus.get(parseInt(messageOrStatus));
        else
            this.finalMessage = this.aMessage;
    }

    ngOnInit() {
    }

    close() {
        this.dialogRef.close();
    }
}
