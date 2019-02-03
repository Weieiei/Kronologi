import { Component, OnInit } from '@angular/core';
import { map } from 'rxjs/operators';
import { UserService } from "../../../../services/user/user.service";
import { UserToDisplay } from "../../../../models/user/UserToDisplay";

@Component({
    selector: 'app-admin-users',
    templateUrl: './admin-users.component.html',
    styleUrls: ['./admin-users.component.scss']
})
export class AdminUsersComponent implements OnInit {

    displayedColumns: string[] = ['id', 'name', 'email', 'services', 'roles'];
    users: UserToDisplay[];

    constructor(private userService: UserService) {
    }

    ngOnInit() {
        this.getAllUsers();
    }

    getAllUsers(): void {
        this.userService.getAllUsers().pipe(
            map(data => {
                return data.map(a => {
                    return new UserToDisplay (
                        a.id, a.firstName, a.lastName, a.email,
                        a.roles, a.employeeServices, a.createdAt, a.updatedAt
                    );
                });
            })
    ).subscribe(
            res => {
                            this.users = res;
                            console.log(this.users);
                        },
            err => console.log(err)
        );
    }

    generateServicesDisplayString(servicesDetailed: any[]): string {
        let outputString: string = "";
        if (servicesDetailed) {
            servicesDetailed.forEach((service) => {
                outputString += service.id + ", ";
            });
            outputString = outputString.substring(0, outputString.length - 2)
        }
        return outputString;
    }

    generateRolesDisplayString(rolesDetailed: any[]): string {
        let outputString: string = "";
        if (rolesDetailed) {
            rolesDetailed.forEach((individualRole) => {
                outputString += individualRole.role + ", ";
            });
            outputString = outputString.substring(0, outputString.length - 2)
        }
        return outputString;
    }
}
