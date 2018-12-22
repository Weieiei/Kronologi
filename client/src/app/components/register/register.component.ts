import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ActivatedRoute, Data, Router } from '@angular/router';
import { UserRegister } from '../../models/user/UserRegister';
import { Service } from '../../models/service/Service';
import { ServiceService } from '../../services/service/service.service';
import { EmployeeRegister } from '../../models/user/EmployeeRegister';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

    /**
     * This is an object that stores some arbitrary data associated to some specific path.
     * Defined for the relevant paths in the app routing module.
     * In this case, since this component is shared by both the regular registration page and the employee creation page, we observe the
     * data to determine which one we're going for.
     */
    data: Data;

    user: UserRegister;
    employee: EmployeeRegister;

    firstName: string;
    lastName: string;
    email: string;
    username: string;
    password: string;

    repeatPassword: string;

    /**
     * First get the list of all services offered by the spa.
     * Then keep track of just the id's of those services that the employee will be assigned.
     */
    services: Service[];
    employeeServices: number[];

    @ViewChild('firstNameInput') firstNameInput: ElementRef;

    constructor(
        private authService: AuthService,
        private serviceService: ServiceService,
        private router: Router,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.route.data.subscribe(data => {
            this.data = data;
            if (this.data.type === AuthService.registerEmployee) {
                this.getServices();
                this.employeeServices = [];
            }
        });
        this.firstNameInput.nativeElement.focus();
    }

    registerUser() {

        if (this.password === this.repeatPassword) {

            if (this.data.type === AuthService.registerClient) {
                this.registerClient();
            } else if (this.data.type === AuthService.registerEmployee) {
                this.registerEmployee();
            }

        } else {
            alert('The passwords don\'t match.');
        }

    }

    registerClient() {

        this.user = new UserRegister(this.firstName, this.lastName, this.email, this.username, this.password);

        this.authService.registerClient(this.user).subscribe(
            res => {
                this.authService.setToken(res['token']);
                this.authService.verifyAdminStatus();
                this.router.navigate(['']);
            },
            err => console.log(err)
        );

    }

    registerEmployee() {

        this.employee = new EmployeeRegister(
            this.firstName, this.lastName, this.email, this.username, this.password, this.employeeServices
        );

        this.authService.registerEmployee(this.employee).subscribe(
            res => {
                alert('Successfully created employee.');
            },
            err => console.log(err)
        );

    }

    getServices() {
        this.serviceService.getServices().subscribe(
            res => this.services = res,
            err => console.log(err)
        );
    }

}
