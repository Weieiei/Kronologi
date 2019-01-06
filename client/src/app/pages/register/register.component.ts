import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ActivatedRoute, Data, Router } from '@angular/router';
import { Service } from '../../models/service/Service';
import { ServiceService } from '../../services/service/service.service';
import { EmployeeShiftTimes } from '../../models/shift/EmployeeShiftTimes';
import { UserRegisterDTO } from '../../interfaces/user-register-dto';
import { UserService } from '../../services/user/user.service';

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

    firstName: string;
    lastName: string;
    email: string;
    password: string;

    confirmPassword: string;

    /**
     * First get the list of all services offered by the spa.
     * Then keep track of just the id's of those services that the employee will be assigned.
     */
    services: Service[];
    employeeServices: number[];
    employeeShifts: EmployeeShiftTimes[];

    arr = Array;
    numberOfShifts = 1;

    isPasswordVisible = false;

    constructor(
        private userService: UserService,
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
                this.employeeShifts = [];
            }
        });
    }

    registerUser(): void {

        if (this.password === this.confirmPassword) {

            if (this.data.type === AuthService.registerClient) {
                this.register();
            } else if (this.data.type === AuthService.registerEmployee) {
                this.registerEmployee();
            }

        } else {
            alert('The passwords don\'t match.');
        }

    }

    register(): void {
        const payload: UserRegisterDTO = {
            firstName: this.firstName,
            lastName: this.lastName,
            email: this.email,
            password: this.password
        };

        this.userService.register(payload).subscribe(
            res => {
                const user = res['user'];
                const token = res['token'];

                this.userService.setUser(user);
                this.userService.setToken(token);

                this.router.navigate(['']);
            },
            err => console.log(err)
        );
    }

    registerEmployee(): void {

        // this.employee = new EmployeeRegister(
        //     this.firstName, this.lastName, this.email, this.password, this.employeeServices, this.employeeShifts
        // );
/*
        this.authService.registerEmployee(this.employee).subscribe(
            res => alert(res['message']),
            err => console.log(err)
        );*/

    }

    getServices(): void {
        this.serviceService.getServices().subscribe(
            res => this.services = res,
            err => console.log(err)
        );
    }

    addShiftComponent(): void {
        this.numberOfShifts++;
    }

    addShift(shift: EmployeeShiftTimes): void {

        /**
         * A user might modify an already set shift, so if that's the case, we delete the old one first.
         */
        const shiftIndex = this.employeeShifts.findIndex(s => s.getNumber() === shift.getNumber());
        if (shiftIndex !== -1) {
            this.employeeShifts.splice(shiftIndex, 1);
        }

        this.employeeShifts.push(shift);

    }

    togglePasswordVisibility() {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}
