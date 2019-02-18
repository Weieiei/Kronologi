import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../services/admin/admin.service';
import { Router } from '@angular/router';
import { AdminEmployeeDTO } from '../../../../interfaces/employee/admin-employee-dto';

@Component({
    selector: 'app-admin-employees',
    templateUrl: './admin-employees.component.html',
    styleUrls: ['./admin-employees.component.scss']
})
export class AdminEmployeesComponent implements OnInit {

    columns: string[] = ['firstName', 'lastName', 'email', 'shifts'];
    employees: AdminEmployeeDTO[];

    constructor(
        private adminService: AdminService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.getAllEmployees();
    }

    getAllEmployees(): void {
        this.adminService.getAllEmployees().subscribe(
            res => this.employees = res
        );
    }

    goToEmployee(id: number): void {
        this.router.navigate(['admin', 'employees', id, 'shifts']);
    }
}
