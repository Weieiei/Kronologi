import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-terms-and-services-dialog',
  templateUrl: './terms-and-services-dialog.component.html',
  styleUrls: ['./terms-and-services-dialog.component.scss']
})
export class TermsAndServicesDialogComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<TermsAndServicesDialogComponent>) { }

  ngOnInit() {
  }

  closeDialog() : void {
    this.dialogRef.close();
  }
}
