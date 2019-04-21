import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {ServiceDTO} from "../../../../interfaces/service/service-dto";
import {MatDatepickerInputEvent, MatDialogConfig, MatDialog} from "@angular/material";
import {AppointmentService} from "../../../../services/appointment/appointment.service";
import {EmployeeFreeTime} from "../../../../interfaces/employee/employee-free-time";
import {TimeDTO} from "../../../../interfaces/date-and-time/TimeDTO";
import {BookAppointmentDTO} from '../../../../interfaces/appointment/book-appointment-dto';
import {ActivatedRoute, Router} from "@angular/router";
import { PaymentDialogComponent } from 'src/app/components/payment-dialog/payment-dialog.component';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})
export class BookComponent implements OnInit {
    days : string[] =['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    months : string[] = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    firstFormGroup: FormGroup;
    secondFormGroup: FormGroup;
    thirdFormGroup: FormGroup;
    isOptional = false;
    service: ServiceDTO;
    date: any;
    dateString: string;
    time: any;
    monthsMap: Map <number, number[]>;
    daysMap: Map <number, Array<EmployeeFreeTime>>;
    employeeId: number;
    appointment: BookAppointmentDTO;
    businessId: number;
    constructor( private dialog: MatDialog, public route: ActivatedRoute, private router: Router, private _formBuilder: FormBuilder, private appointmentService: AppointmentService) {
    }

    ngOnInit() {
        this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"))

        this.firstFormGroup = this._formBuilder.group({
            firstCtrl: ''
        });
        this.secondFormGroup = this._formBuilder.group({
            secondCtrl: ''
        });
        this.thirdFormGroup = this._formBuilder.group({
            thirdCtrl: ''
        })
    }

    setService(service: ServiceDTO): void {
        this.service = service;
      //  this.getAllAvailabilitiesForService(this.service);
    }

    serviceIsSelected(): boolean {
        return !!this.service;
    }

    setDate(date: any) {
        this.date = date;
        let dayOfWeek : string = this.days[date.getDay()];
        let month : string = this.months[date.getMonth()];
        let dayOfMonth : string = date.getDate();
        let yearOfDate : string = date.getFullYear();
        this.dateString = dayOfWeek + " " + month + " " + dayOfMonth + ", " + yearOfDate;

    }

    setIsPayment(client_pays : any){
        console.log("hello")
        console.log(client_pays)
        if(client_pays){
            const dialogConfig = new MatDialogConfig();

            dialogConfig.disableClose = true;
            dialogConfig.autoFocus = true;
            dialogConfig.width = '400px';
            dialogConfig.height = '400px';

            dialogConfig.data = {
                service: this.service,
                businessId: this.businessId
            };
            const dialogRef = this.dialog.open(PaymentDialogComponent, dialogConfig);

            dialogRef.afterClosed().subscribe(business => {
            });
        }
    }
    dateIsSelected(): boolean{
        return !!this.date;
    }

    setTime(time: String) {
        this.time = time;
    }

    timeIsSelected(): boolean {
        return !!this.time;
    }



    selectStartTime(startTime :  TimeDTO){

    }

    getAllAvailabilitiesForService(service: ServiceDTO) {
        this.monthsMap = new Map<number, number[]>();
        this.daysMap = new Map<number, Array<EmployeeFreeTime>>();

        this.appointmentService.getAvailabilitiesForService(service.id).subscribe(
            res => {
                for (let eachEmployee of res) {
                    let emp_id;
                    let emp_name;
                    for (let key in eachEmployee) {
                        if (key == 'employee_id') {
                            emp_id = eachEmployee[key];
                        }
                        if (key == 'employee') {
                            emp_name = eachEmployee[key];
                        }

                        if (key == 'Availabilities') {
                            for (let each of eachEmployee[key]) {
                                // for (let availabilityKey in each){
                                let thisYear = new Date().getFullYear();
                                let dayOfMonth: number;
                                let dayOfYear: number;
                                let month: number;
                                if (each['date']['year'] == thisYear.toString()) {
                                    //monthsMap
                                    month = each['date']['monthValue'];
                                    if (!this.monthsMap.has(each['date']['monthValue'])) {
                                        let arr: number[] = [];
                                        dayOfMonth = parseInt(each['date']['dayOfMonth']);
                                        arr.push(dayOfMonth);

                                        this.monthsMap.set(parseInt(each['date']['monthValue']), arr);
                                    }
                                    //if the month key is already in the map, add the day in the amp
                                    else if (this.monthsMap.has(each['date']['monthValue'])) {
                                        let arrayOfDays = this.monthsMap.get(parseInt(each['date']['monthValue']));
                                        dayOfMonth = parseInt(each['date']['dayOfMonth']);
                                        arrayOfDays.push(dayOfMonth);
                                        this.monthsMap.set(parseInt(each['date']['monthValue']), arrayOfDays);
                                    }
                                    //daysMap
                                    if (!this.daysMap.has(each['date']['dayOfYear'])) {
                                        let arr: EmployeeFreeTime[] = [];
                                        dayOfYear = parseInt(each['date']['dayOfYear']);

                                        //create EmployeeFreeTime and add to array as value to key
                                        // dayOfYear
                                        const empFreeTime: EmployeeFreeTime = {
                                            employee_name: emp_name,
                                            employee_id: emp_id,
                                            startTime: each['startTime'],
                                            endTime: each['endTime'],
                                        };
                                        arr.push(empFreeTime);

                                        this.daysMap.set(parseInt(each['date']['dayOfYear']), arr);

                                    }
                                    //if the DAY key is already in the map, add the day in the amp
                                    else if (this.daysMap.has(each['date']['dayOfYear'])) {
                                        let arrayOfEmployeeFreeTimes = this.daysMap.get(parseInt(each['date']['dayOfYear']));
                                        dayOfYear = parseInt(each['date']['dayOfYear']);
                                        const empFreeTime: EmployeeFreeTime = {
                                            employee_name: emp_name,
                                            employee_id: emp_id,
                                            startTime: each['startTime'],
                                            endTime: each['endTime'],
                                        };
                                        arrayOfEmployeeFreeTimes.push(empFreeTime);
                                        this.daysMap.set(parseInt(each['date']['dayOfYear']), arrayOfEmployeeFreeTimes);

                                    }
                                }
                            }
                        }
                    }
                }
       console.log(this.daysMap);

        });
    }

    bookAppointment() {
        this.appointment = {
            employeeId: this.employeeId,
            serviceId: this.service.id,
            date: this.date,
            startTime: this.time
        };
        this.appointmentService.bookAppointment(this.appointment).subscribe(
            res => console.log(res)
        );
    }

    setTimeAndEmployeeId(map: Map<number, string>) {
        map.forEach((value: string, key: number) => {
            this.employeeId = key;
            this.time = value;
        });
    }

    goBack() {
        this.router.navigate(['/home']);
    }
}

