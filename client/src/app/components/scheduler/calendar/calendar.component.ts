import { Component, Input, OnInit, OnChanges } from '@angular/core';
import { SlidePanelComponent } from '../slide-panel/slide-panel.component';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit, OnChanges {

  @Input() year: number;
  @Input() month: number;
  @Input() slider: SlidePanelComponent;

  daysOfWeek = [
    'Sunday',
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday'
  ];

  spacer: number;
  days: any[] = [];

  private currentDate: Date = new Date();

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges() {
    this.updateCalendar();
  }

  updateCalendar() {
    this.days = [];
    this.setSpacerLength();
    this.setNumberOfDays();
  }

  chooseDate(date: any) {
    if (date.enabled) {
      this.slider.goRight();
    }
  }

  private setNumberOfDays(): void {
    const numOfDays = new Date(this.year, this.month, 0).getDate();

    for (let i = 1; i <= numOfDays; i++) {
      this.days.push({date: i, enabled: this.isEnabled(i)});
    }
  }

  private isEnabled(date: number): boolean {
    if (this.year > this.currentDate.getFullYear()) {
      return true;
    } else {
      const yearGood = this.year >= this.currentDate.getFullYear();

      if (yearGood && this.month > this.currentDate.getMonth() + 1) {
        return true;
      } else if (yearGood && this.month >= this.currentDate.getMonth() + 1 && date >= this.currentDate.getDate()) {
        return true;
      }
    }

    return false;
  }

  private setSpacerLength(): void {
    this.spacer = new Date(this.year, this.month - 1, 1).getDay();
  }
}
