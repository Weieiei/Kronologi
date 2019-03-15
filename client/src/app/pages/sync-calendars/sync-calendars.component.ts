import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../services/appointment/appointment.service'
import { ThemeService } from "../../core/theme/theme.service";

@Component({
  selector: 'app-sync-calendars',
  templateUrl: './sync-calendars.component.html',
  styleUrls: ['./sync-calendars.component.scss']
})
export class SyncCalendarsComponent implements OnInit {

  darkModeActive: boolean;
  
  constructor(private appointmentService: AppointmentService, private themeService: ThemeService) { }

  ngOnInit() {
    this.themeService.darkModeState.subscribe(value => {
      this.darkModeActive = value;
    })
  }

  login(){
    this.appointmentService.googleLogin().subscribe(result=>{
        window.location.href = result['url'];
    })
}
}
