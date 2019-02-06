import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import * as moment from 'moment';
import { NewShiftDTO } from '../../../../../interfaces/shift/new-shift-dto';

@Component({
    selector: 'app-add-shift-form',
    templateUrl: './add-shift-form.component.html',
    styleUrls: ['./add-shift-form.component.scss']
})
export class AddShiftFormComponent implements OnInit {

    minDate: Date = new Date();
    maxDate: Date = new Date(moment(this.minDate).add(5, 'months').format());

    date: string;

    startHour: number;
    startMinute: number;

    endHour: number;
    endMinute: number;

    @Output() shiftChange = new EventEmitter();

    constructor() {
    }

    ngOnInit() {
    }

    setShift() {

        const date = new Date(this.date);

        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();

        const shift: NewShiftDTO = {
            date: `${year}-${month < 10 ? '0' + month : month}-${day < 10 ? '0' + day : day}`,
            startTime: `${this.startHour < 10 ? '0' + this.startHour : this.startHour}:${this.startMinute < 10 ? '0' + this.startMinute : this.startMinute}`,
            endTime: `${this.endHour < 10 ? '0' + this.endHour : this.endHour}:${this.endMinute < 10 ? '0' + this.endMinute : this.endMinute}`
        };

        this.shiftChange.emit(shift);

        this.date = void 0;
        this.startHour = void 0;
        this.startMinute = void 0;
        this.endHour = void 0;
        this.endMinute = void 0;

    }

}
