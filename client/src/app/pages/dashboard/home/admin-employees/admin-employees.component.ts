import { Component, OnInit } from '@angular/core';
import {User} from "../../../../interfaces/user";
import {AdminService} from "../../../../services/admin/admin.service";

@Component({
  selector: 'app-admin-employees',
  templateUrl: './admin-employees.component.html',
  styleUrls: ['./admin-employees.component.scss']
})
export class AdminEmployeesComponent implements OnInit {

    users: User[];

  constructor(private adminService: AdminService) { }

    submit() {
      alert("ID: ");
    }

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
