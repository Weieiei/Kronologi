import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-routing-and-account-help',
  templateUrl: './routing-and-account-help.component.html',
  styleUrls: ['./routing-and-account-help.component.scss']
})
export class RoutingAndAccountHelpComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<RoutingAndAccountHelpComponent>) { }

  ngOnInit() {
  }

  closeDialog() : void {
    this.dialogRef.close();
  }

}
