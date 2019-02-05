import { Component, OnInit } from '@angular/core';
import {AdminService} from "../../../../services/admin/admin.service";
import {User} from "../../../../interfaces/user";

@Component({
  selector: 'app-shift',
  templateUrl: './shift.component.html',
  styleUrls: ['./shift.component.scss']
})
export class ShiftComponent implements OnInit {

  constructor(private adminService: AdminService) { }

  ngOnInit() {
  }



}
