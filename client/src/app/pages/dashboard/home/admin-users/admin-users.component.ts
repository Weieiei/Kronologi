import {Component, OnInit} from '@angular/core';
import {map} from 'rxjs/operators';
import {UserService} from '../../../../services/user/user.service';
import {UserToDisplay} from '../../../../models/user/UserToDisplay';
import {MatDialog} from '@angular/material';
import {AssignServicesDialogComponent} from './assign-services-dialog/assign-services-dialog.component';
import {ServiceService} from '../../../../services/service/service.service';
import {Service} from '../../../../models/service/Service';
import {ChangeClientToEmployeeDialogComponent} from './change-client-to-employee-dialog/change-client-to-employee-dialog.component';
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-admin-users',
    templateUrl: './admin-users.component.html',
    styleUrls: ['./admin-users.component.scss']
})


export class AdminUsersComponent implements OnInit {

    displayedColumns: string[] = ['id', 'name', 'email', 'services', 'roles', 'actions'];
    users: UserToDisplay[];
    services: Service[];
    businessId: number;

    componentState: {
        users: Array<UserToDisplay>,
        currentPage: number,
        itemsPerPage: number,
        search: string,
        totalItems: number,
    };

    constructor(private userService: UserService,
                private serviceService: ServiceService,
                public dialog: MatDialog,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"));
        this.componentState = {
            users: [],
            currentPage: 1,
            itemsPerPage: 10,
            search: '',
            totalItems: 0,
        };

        this.getAllUsers();
        this.userService.getAllUsers(this.businessId).subscribe(
           res=>{
               console.log(res);
           }
        );
        this.getAllServices();
    }

    getAllUsers(): void {
        this.userService.getAllUsers(this.businessId).pipe(
            map(data => {
                return data.map(a => {
                    return new UserToDisplay (
                        a.id, a.firstName, a.lastName, a.email,
                        a.role, a.services, a.createdAt, a.updatedAt
                    );
                });
            })
    ).subscribe(
            res => {
                this.users = res;
                this.componentState.users = res;
                this.updateServices(this.users);
            },
            err => console.log(err)
        );
    }

    generateServicesDisplayString(servicesDetailed: any[]): string {
        let outputString = '';
        if (servicesDetailed) {
            servicesDetailed.forEach((service) => {
                outputString += service.id + ', ';
            });
            outputString = outputString.substring(0, outputString.length - 2);
        }
        return outputString;
    }

    generateRolesDisplayString(rolesDetailed: any[]): string {
        let outputString = '';
        if (rolesDetailed) {
            rolesDetailed.forEach((individualRole) => {
                outputString += individualRole.role + ', ';
            });
            outputString = outputString.substring(0, outputString.length - 2);
        }
        return outputString;
    }

    isEmployee(user: UserToDisplay): boolean {
        return user.userRole ==='EMPLOYEE' || user.userRole ==='ADMIN';
    }

    isClient(user: UserToDisplay): boolean {
       return user.userRole ==='CLIENT';
    }

    openAddServiceDialog(user: any) {
        const dialogRef = this.dialog.open(AssignServicesDialogComponent, {
            width: '250px',
            data: {services: this.services, user: user}
        }).afterClosed().subscribe(result => {
                this.getAllUsers();
            }
        );
    }

    openChangeClientToEmployeeDialog(user: any) {
        const dialogRef = this.dialog.open(ChangeClientToEmployeeDialogComponent, {
            width: '250px',
            data: {user: user}
        }).afterClosed().subscribe(result => {
                this.getAllUsers();
            }
        );
    }

    getAllServices(): void {
        this.serviceService.getPlainServices(this.businessId).pipe(
            map(data => {
                return data.map(a => {
                    return a;
                });
            })
        ).subscribe(
            res => this.services = res,
            err => console.log(err)
        );
    }

    filterItems(items: Array<UserToDisplay>, search: string): Array<UserToDisplay> {
        if (search.length < 1) {
            return items;
        }

        const searchCriteria = search.toLowerCase();
        return items.filter((item) => item.firstName.toLowerCase().includes(searchCriteria)
            || item.lastName.toLowerCase().includes(searchCriteria) );
    }

    updateServices(allItems: Array<UserToDisplay>): void {
        if (allItems) {
            const filteredItems = this.filterItems(allItems, this.componentState.search);
            const totalItems = filteredItems.length;
            this.componentState.users = filteredItems;
            this.componentState.totalItems = totalItems;
        }
    }


    onSearch(searchTerm: string) {
        this.componentState.currentPage = 1;
        this.componentState.search = searchTerm;
        this.updateServices(this.users);
    }

}
