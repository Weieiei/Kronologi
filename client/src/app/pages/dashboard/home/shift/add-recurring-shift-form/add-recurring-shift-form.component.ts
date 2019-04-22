import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { NewShiftDTO } from '../../../../../interfaces/shift/new-shift-dto';
import {ThemeService} from "../../../../../core/theme/theme.service";

@Component({
  selector: 'app-add-recurring-shift-form',
  templateUrl: './add-recurring-shift-form.component.html',
  styleUrls: ['./add-recurring-shift-form.component.scss']
})
export class AddRecurringShiftFormComponent implements OnInit {
    date: string;
    days: Array<string> = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    daysSelect: Set<string> = new Set();
    relativeToday: Set<number> = new Set<number>();
    occurrences: number;
    startHour: number;
    startMinute: number;
    @Output() shiftChange = new EventEmitter();
    darkModeActive: boolean;
    endHour: number;
    endMinute: number;
  constructor(    private themeService :ThemeService
  ) {

  }

  ngOnInit() {
      this.themeService.darkModeState.subscribe(value => {
          this.darkModeActive = value;
      })
  }

    onChange(day: string) {
        const todayDate = new Date();
      if (this.daysSelect.has(day)) {
          this.daysSelect.delete(day);
          this.relativeToday.delete(this.days.indexOf(day) - todayDate.getDay());
      } else {
          this.daysSelect.add(day);
          this.relativeToday.add(this.days.indexOf(day) - todayDate.getDay() );
      }
    }
  createRecurringDate(): void {
      // make your http call using the daysSelect
      const todayDate = new Date();
      const shifts: Array<NewShiftDTO> = new Array();
      const curr: Date = new Date();
      let year;
      let month;
      let day;
      const relativeTodayArray: Array<number> = Array.from(this.relativeToday.values());
      let currRel: number;

      for (let j = 0; j < this.occurrences; j++) {
          for (let i = 0; i <   this.daysSelect.size; i++) {
              // calculate difference between today and recurrence date
              currRel = relativeTodayArray[i] + j * 7;
              // Date not in past
              if (currRel > 0) {
                  curr.setDate(todayDate.getDate() + currRel);
                  year = curr.getFullYear();
                  month = curr.getMonth();
                  day = curr.getDay();
                  const shift: NewShiftDTO = {
                      date: `${year}-${month < 10 ? '0' + month : month}-${day < 10 ? '0' + day : day}`,
                      startTime: `${this.startHour < 10 ? '0' + this.startHour : this.startHour}:${this.startMinute < 10 ? '0' + this.startMinute : this.startMinute}`,
                      endTime: `${this.endHour < 10 ? '0' + this.endHour : this.endHour}:${this.endMinute < 10 ? '0' + this.endMinute : this.endMinute}`
                  };
                  shifts.push(shift);
              }
          }
      }
      this.shiftChange.emit(shifts);

      this.date = void 0;
      this.startHour = void 0;
      this.startMinute = void 0;
      this.endHour = void 0;
      this.endMinute = void 0;
      this.occurrences = void 0;
  }
}
