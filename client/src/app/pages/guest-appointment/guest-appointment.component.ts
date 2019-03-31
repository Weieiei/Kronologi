import { Component, OnInit } from '@angular/core';
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'app-guest-appointment',
  templateUrl: './guest-appointment.component.html',
  styleUrls: ['./guest-appointment.component.scss']
})
export class GuestAppointmentComponent implements OnInit {

    firstName: string;
    lastName: string;
    email: string;

  constructor(
      private userService: UserService
  ) { }

  ngOnInit() {
  }

}
