import { Component, OnInit, Inject } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import { SnackBar } from 'src/app/snackbar';

@Component({
  selector: 'app-msg-dialog',
  templateUrl: './msg-dialog.component.html',
  styleUrls: ['./msg-dialog.component.scss']
})

export class MessageDialogComponent implements OnInit {
    serviceName:string;
    isEmployee:boolean;
    isAdmin:boolean;
    aTitle:string;
    aMessage:string;

    constructor(
        private dialogRef: MatDialogRef<MessageDialogComponent>,
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
