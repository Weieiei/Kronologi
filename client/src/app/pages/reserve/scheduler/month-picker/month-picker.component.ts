import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
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

  @Input() year: number;
  @Output() yearChange = new EventEmitter();
  @Input() month: number;
  @Output() monthChange = new EventEmitter();

  options = [];
  dropdownIndex: number;

  constructor() {
  }

  ngOnInit() {
    this.dropdownIndex = 0;
    this.initOptions();
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
    this.yearChange.emit(this.year);
  }

  setMonth(): void {
    this.month = this.options[this.dropdownIndex].month;
    this.monthChange.emit(this.month);
  }

  private initOptions() {
    for (let i = 0; i < this.MONTHS_IN_ADVANCE; i++) {
      const currentMonth = i + this.month;
      this.options.push({
        index: i,
        month: currentMonth % 12,
        year: this.year + Math.floor(currentMonth / 12)
      });
    }
  }
}
