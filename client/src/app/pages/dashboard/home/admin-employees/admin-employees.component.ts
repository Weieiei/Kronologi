import {Component, OnInit} from '@angular/core';
import {AdminService} from '../../../../services/admin/admin.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AdminEmployeeDTO} from '../../../../interfaces/employee/admin-employee-dto';

@Component({
    selector: 'app-admin-employees',
    templateUrl: './admin-employees.component.html',
    styleUrls: ['./admin-employees.component.scss']
})
export class AdminEmployeesComponent implements OnInit {
    businessId: number;
    columns: string[] = ['firstName', 'lastName', 'email', 'shifts'];
    employees: AdminEmployeeDTO[];

    componentState: {
        employees: Array<AdminEmployeeDTO>,
        currentPage: number,
        itemsPerPage: number,
        search: string,
        totalItems: number,
    };

    constructor(
        private adminService: AdminService,
        private router: Router,
        private route : ActivatedRoute,
    ) {
    }

    ngOnInit() {
        this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"));
        this.componentState = {
            employees: [],
            currentPage: 1,
            itemsPerPage: 10,
            search: '',
            totalItems: 0,
        };

        this.getAllEmployees(this.businessId);
    }

    getAllEmployees(businessId: number): void {
        this.adminService.getAllEmployees(businessId).subscribe(
            res => {
                this.employees = res;
                this.componentState.employees = res;
                this.updateEmployees(this.employees);
            },
            err => console.log(err)
        );
    }

    goToEmployee(id: number): void {
        this.router.navigate([this.businessId,'admin', 'employees', id, 'shifts']);
    }

    filterItems(items: Array<AdminEmployeeDTO>, search: string): Array<AdminEmployeeDTO> {
        if (search.length < 1) {
            return items;
        }

        const searchCriteria = search.toLowerCase();
        return items.filter((item) => item.firstName.toLowerCase().includes(searchCriteria)
            || item.lastName.toLowerCase().includes(searchCriteria)
            || item.email.toLowerCase().includes(searchCriteria));
    }

    updateEmployees(allItems: Array<AdminEmployeeDTO>): void {
        if (allItems) {
            const filteredItems = this.filterItems(allItems, this.componentState.search);
            const totalItems = filteredItems.length;
            this.componentState.employees = filteredItems;
            this.componentState.totalItems = totalItems;
        }
    }


    onSearch(searchTerm: string) {
        this.componentState.currentPage = 1;
        this.componentState.search = searchTerm;
        this.updateEmployees(this.employees);
    }
}
