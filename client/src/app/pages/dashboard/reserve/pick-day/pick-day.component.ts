import {AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {MatDatepickerInputEvent} from "@angular/material";
import {ServiceService} from "../../../../services/service/service.service";
import {ServiceDTO} from "../../../../interfaces/service/service-dto";
import {AppointmentService} from "../../../../services/appointment/appointment.service";
import {EmployeeFreeTime} from "../../../../interfaces/employee/employee-free-time";

@Component({
  selector: 'app-pick-day',
  templateUrl: './pick-day.component.html',
  styleUrls: ['./pick-day.component.scss']
})

export interface Slot {
    startTime: string;
    employeeId: number;
    employeeName: string;
}

export class PickDayComponent implements OnInit, OnChanges, AfterViewInit{


    dateEvents: string[] = [];
    @Output() dateChange = new EventEmitter();
    @Input() childService: ServiceDTO;
    @Input() childMonthsMap: Map <number, number[]>;
    @Input() childDaysMap: Map <number, Array<EmployeeFreeTime>>;x
    startTimes: string[] = ["10:00", "11:00", "12:00"];
    appointmentService: AppointmentService;
    startTimesMap: Map<string, string> = new Map<string, string>();

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
    };

    addEvent(type: string, event: MatDatepickerInputEvent<Date>) {
        this.dateEvents.push(`${type}: ${event.value}`);
        this.dateChange.emit(this.dateEvents[this.dateEvents.length-1]);
    }

    slotGenerator (employeeFreeTime: EmployeeFreeTime, serviceDuration): Array<Slot> {
        const numberOfSlots: number = Math.floor((employeeFreeTime.endTime - serviceDuration - employeeFreeTime) / 10);
        const generatedSlots: Array<Slot> = [];
        for (let i=0; i < numberOfSlots; i++) {
            const startTime = (employeeFreeTime + i*10).toString;
            const slot: Slot = {
                startTime: startTime,
                employeeId: employeeFreeTime.employee_id,
                employeeName: employeeFreeTime.employee_name
            };
            generatedSlots.push(slot);
        }
        return generatedSlots;
    }

    // selectStartTime(service : ServiceDTO){
    //     this.appointmentService.getAvailabilitiesForService(service).subscribe(
    //         res => {
    //             console.log(res);
    //         }
    //     )
    // }

    ngOnChanges(changes: SimpleChanges): void {
        if (this.childService!=null)
            console.log(this.childService.id);
    }

    ngAfterViewInit(): void {
    }
}
