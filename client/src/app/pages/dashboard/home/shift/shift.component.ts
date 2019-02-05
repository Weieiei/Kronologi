import { Component, OnInit } from '@angular/core';
import {AdminService} from "../../../../services/admin/admin.service";
import {User} from "../../../../interfaces/user";

@Component({
  selector: 'app-shift',
  templateUrl: './shift.component.html',
  styleUrls: ['./shift.component.scss']
})
export class ShiftComponent implements OnInit {

    users: User[];

  constructor(private adminService: AdminService) { }

  ngOnInit() {
      this.getAllEmployees();
  }

    getAllEmployees(): void {
      this.users = [];
        this.adminService.getEmployeeShifts().subscribe(
            res => {
                console.log(res);
                for (const user of res) {

                        this.users.push(user);

                }
            },
            err => console.log(err)
        );
    }

}
