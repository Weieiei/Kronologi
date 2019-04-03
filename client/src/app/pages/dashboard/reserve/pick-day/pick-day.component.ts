import {AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material';
import {ServiceService} from '../../../../services/service/service.service';
import {ServiceDTO} from '../../../../interfaces/service/service-dto';
import {AppointmentService} from '../../../../services/appointment/appointment.service';
import {EmployeeFreeTime} from '../../../../interfaces/employee/employee-free-time';

export interface Slot {
    startTime: string;
    employeeId: number;
    employeeName: string;
}

@Component({
  selector: 'app-pick-day',
  templateUrl: './pick-day.component.html',
  styleUrls: ['./pick-day.component.scss']
})
export class PickDayComponent implements OnInit, OnChanges, AfterViewInit{


    dateEvents: Date[] = [];
    @Output() dateChange = new EventEmitter();
    @Output() timeChangeAndEmployeeChange = new EventEmitter();
    @Input() childService: ServiceDTO;
    @Input() childMonthsMap: Map <number, number[]>;
    @Input() childDaysMap: Map <number, Array<EmployeeFreeTime>>;
    startSlots: Slot[] = [];
    appointmentService: AppointmentService;
    startTimesMap: Map<string, string> = new Map<string, string>();

    itemsPerPageOptions: Array<number> = [4, 8, 16, 32, 64];

    componentState: {
        totalItems: number,
        currentPageSize: number;
        currentPage: number;
    };

    constructor() {
        this.componentState = {
            totalItems: 0,
            currentPageSize: 8,
            currentPage: 1,
        };

    }

    ngOnInit() {
    }

    dateFilter = (d: Date): boolean => {
        const day = d.getDate();
        const month = d.getMonth() + 1;

        if (this.childMonthsMap.get(month)) {
            return this.childMonthsMap.get(month).includes(day);
        } else {
            return false;
        }
    }

    addEvent(event: MatDatepickerInputEvent<Date>) {
        if (event.value) {
            this.dateEvents.push(event.value);
            this.dateChange.emit(this.dateEvents[this.dateEvents.length - 1]);
            this.startSlots = this.slotGenerator(this.calculateCurrentDayKey());
        }
    }

    slotGenerator (selectedDay: number): Array<Slot> {
        const generatedSlots: Array<Slot> = [];

        this.childDaysMap.get(selectedDay).forEach((value: EmployeeFreeTime) => {
            const numberOfSlots: number = Math.floor((this.calculateTimeInMinutes(value.endTime) - this.childService.duration
                - this.calculateTimeInMinutes(value.startTime)) / 10);
            for (let i = 0; i <= numberOfSlots; i++) {
                const startTime = this.calculateTimeString((this.calculateTimeInMinutes(value.startTime) + (i * 10)));
                const slot: Slot = {
                    startTime: startTime,
                    employeeId: value.employee_id,
                    employeeName: value.employee_name
                };
                generatedSlots.push(slot);
            }

        });
        return generatedSlots;
    }

    calculateCurrentDayKey(): number {
        const currentDay = this.dateEvents[this.dateEvents.length - 1];

        const dayCount = [0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334];
        const mn = currentDay.getMonth();
        const dn = currentDay.getDate();
        let dayOfYear = dayCount[mn] + dn;
        if (mn > 1 && this.isLeapYear(currentDay)) {
            dayOfYear++;
        }
        return dayOfYear;
    }

    isLeapYear(date: Date): boolean {
        const year = date.getFullYear();
        if ((year && 3) !== 0) {
            return false;
        }
        return ((year % 100) !== 0 || (year % 400) === 0);
    }

    calculateTimeInMinutes (timeString: string): number {
        const timeArray = timeString.split(':');
        return parseInt(timeArray[0], 10) * 60 + parseInt(timeArray[1], 10);
    }

    calculateTimeString (timeInMinutes: number): string {
        const hourString: string = Math.floor(timeInMinutes / 60).toString();
        let minuteString: string = (timeInMinutes % 60).toString();
        if (timeInMinutes % 60 === 0) {
            minuteString = '00';
        }
        return hourString + ':' + minuteString;
    }


    // selectStartTime(service : ServiceDTO){
    //     this.appointmentService.getAvailabilitiesForService(service).subscribe(
    //         res => {
    //             console.log(res);
    //         }
    //     )
    // }

    ngOnChanges(changes: SimpleChanges): void {
        if (this.childService != null) {
            console.log(this.childService.id);
        }
    }

    ngAfterViewInit(): void {
    }

    // printPageEvent($event) {
    //     if ($event.pageSize !== this.componentState.currentPageSize) {
    //         this.componentState.currentPage = 1;
    //         this.componentState.currentPageSize = $event.pageSize;
    //         this.getServices();
    //     } else if ($event.pageIndex !== this.componentState.currentPage + 1) {
    //         this.componentState.currentPage = $event.pageIndex + 1;
    //         this.getServices();
    //     }
    // }
    selectStartTimeAndEmployeeId(startTime: string, employeeId: number) {
        const myMap: Map<number, string > = new Map<number, string>();
        myMap.set(employeeId, startTime);
        this.timeChangeAndEmployeeChange.emit(myMap);
    }

}
