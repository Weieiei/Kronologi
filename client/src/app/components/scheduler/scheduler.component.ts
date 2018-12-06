import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-scheduler',
  templateUrl: './scheduler.component.html',
  styleUrls: ['./scheduler.component.scss']
})
export class SchedulerComponent implements OnInit {

  year: number;
  month: number;

  constructor() {
    const currentDate = new Date();
    this.year = currentDate.getFullYear();
    this.month = currentDate.getMonth();
  }

  ngOnInit() {
  }
}
