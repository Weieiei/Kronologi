import {AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {MatDatepickerInputEvent} from "@angular/material";
import {ServiceService} from "../../../../services/service/service.service";
import {ServiceDTO} from "../../../../interfaces/service/service-dto";
import {AppointmentService} from "../../../../services/appointment/appointment.service";

@Component({
  selector: 'app-pick-day',
  templateUrl: './pick-day.component.html',
  styleUrls: ['./pick-day.component.scss']
})
export class PickDayComponent implements OnInit, OnChanges, AfterViewInit{


    dateEvents: string[] = [];
    @Output() dateChange = new EventEmitter();
    @Input() childService: ServiceDTO;
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
        const day = d.getDay();
        //const currentMonth = new Date().getMonth();
        console.log(d.getMonth());


        //for current month day 1 to last day
        //pass the interval of time to the backend
        //backend returns the time slots free

        if (d.getMonth()==1 || d.getMonth()==2)

        //returns days not greyd out
        return day !== 0 && day !== 6;
        else
            return true;
    };

    addEvent(type: string, event: MatDatepickerInputEvent<Date>) {
        this.dateEvents.push(`${type}: ${event.value}`);
        this.dateChange.emit(this.dateEvents[this.dateEvents.length-1]);
    }

    getStartTimes(){

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
