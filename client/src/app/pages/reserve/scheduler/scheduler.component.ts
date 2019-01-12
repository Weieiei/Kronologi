import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-scheduler',
  templateUrl: './scheduler.component.html',
  styleUrls: ['./scheduler.component.scss']
})
export class SchedulerComponent implements OnInit {
  year: number;
  month: number;

  @Output() dateChange = new EventEmitter();

  constructor() {
    const currentDate = new Date();
    this.year = currentDate.getFullYear();
    this.month = currentDate.getMonth();
  }

  ngOnInit() {
  }

  setDay(day: any): void {
    const date: Date = new Date(this.year, this.month, day);
    this.dateChange.emit(date);
  }
}
