import { Component, OnInit } from '@angular/core';
import { NONE_TYPE } from '@angular/compiler/src/output/output_ast';

@Component({
  selector: 'app-business-register',
  templateUrl: './business-register.component.html',
  styleUrls: ['./business-register.component.scss']
})
export class BusinessRegisterComponent implements OnInit {
    selectedFile = null;

    onFileSelected(event) {
     this.selectedFile = event.target.files[0];
    }
    selected

    constructor() { }

    ngOnInit() {
    }

}
