import { Component, Input, OnInit } from '@angular/core';
import { CalendarComponent } from '../calendar/calendar.component';

@Component({
  selector: 'app-month-picker',
  templateUrl: './month-picker.component.html',
  styleUrls: ['./month-picker.component.scss']
})
export class MonthPickerComponent implements OnInit {

  private MONTHS_IN_ADVANCE = 5;

  MONTHS: string[] = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
  ];

  @Input() calendar: CalendarComponent;

  options = [];
  dropdownIndex: number;

  year: number;
  month: number;

  private currentDate: Date;

  constructor() {
  }

  ngOnInit() {
    this.currentDate = new Date();
    this.dropdownIndex = 0;
    this.initOptions();
    this.update();
  }

  nextMonth(): void {
    if (this.dropdownIndex + 1 < this.options.length) {
      this.dropdownIndex++;
    }
    this.update();
  }

  previousMonth(): void {
    if (this.dropdownIndex - 1 >= 0) {
      this.dropdownIndex--;
    }
    this.update();
  }

  update() {
    this.setYear();
    this.setMonth();
  }

  setYear(): void {
    this.year = this.options[this.dropdownIndex].year;
  }

  setMonth(): void {
    this.month = this.options[this.dropdownIndex].month;
  }

  private initOptions() {
    const currentYear = this.currentDate.getFullYear();

    for (let i = 0; i < this.MONTHS_IN_ADVANCE; i++) {
      const currentMonth = i + this.currentDate.getMonth();
      this.options.push({
        index: i,
        month: (currentMonth % 12) + 1,
        year: currentYear + Math.floor(currentMonth / 12)
      });
    }
  }
}
