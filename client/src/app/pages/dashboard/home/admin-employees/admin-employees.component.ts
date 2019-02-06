import { Component, OnInit } from '@angular/core';
import { User } from '../../../../interfaces/user';
import { AdminService } from '../../../../services/admin/admin.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-admin-employees',
    templateUrl: './admin-employees.component.html',
    styleUrls: ['./admin-employees.component.scss']
})
export class AdminEmployeesComponent implements OnInit {

    users: User[];

    constructor(
        private adminService: AdminService,
        private router: Router
    ) {
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

    goToEmployee(id: number) {
        this.router.navigate(['admin', 'employees', id, 'shifts']);
    }
}
