import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../services/admin/admin.service';
import { AdminEmployeeDTO } from '../../../../interfaces/employee/admin-employee-dto';
import { skip, take, mergeMap, switchMap, mapTo } from 'rxjs/operators';
import { merge } from 'rxjs/observable/merge';
import { Observable, Subject } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'app-admin-employees',
    templateUrl: './admin-employees.component.html',
    styleUrls: ['./admin-employees.component.scss']
})
export class AdminEmployeesComponent implements OnInit {
    businessId: number;
    columns: string[] = ['firstName', 'lastName', 'email', 'shifts'];
    employees: AdminEmployeeDTO[];

    employees$: Observable<Array<AdminEmployeeDTO>>;
    showNotification$: Observable<boolean>;
    update$ = new Subject<void>();
    forceReload$ = new Subject<void>();


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
        private route: ActivatedRoute,
    ) {
    }

    ngOnInit() {
        this.businessId = parseInt(this.route.snapshot.paramMap.get('businessId'));
        this.adminService.setBusinessId(this.businessId);
        const initialEmployees$ = this.getDataOnce();

        const updates$ = merge(this.update$, this.forceReload$).pipe(
            mergeMap(() => this.getDataOnce())
        );

        this.componentState = {
            employees: [],
            currentPage: 1,
            itemsPerPage: 10,
            search: '',
            totalItems: 0,
        };

        this.employees$ = merge(initialEmployees$, updates$);
        const reload$ = this.forceReload$.pipe(switchMap(() => this.getNotifications()));
        const initialNotifications$ = this.getNotifications();
        const show$ = merge(initialNotifications$, reload$).pipe(mapTo(true));
        const hide$ = this.update$.pipe(mapTo(false));
        this.showNotification$ = merge(show$, hide$);

        this.getAllEmployees();
    }

    getDataOnce() {
        return this.adminService.allEmployees.pipe(take(1));
    }

    getNotifications() {
        return this.adminService.allEmployees.pipe(skip(1));
    }

    forceReload() {
        this.adminService.forceReload();
        this.forceReload$.next();
    }

    getAllEmployees(): void {
        this.employees$.subscribe(
            res => {
                this.employees = res;
                this.componentState.employees = res;
                this.updateEmployees(this.employees);
            },
            err => console.log(err)
        );
    }

    goToEmployee(id: number): void {
        this.router.navigate([this.businessId, 'admin', 'employees', id, 'shifts']);
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
