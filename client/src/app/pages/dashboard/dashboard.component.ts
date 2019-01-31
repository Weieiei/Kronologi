import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  constructor(private userService: UserService,
    private router: Router ) { }

ngOnInit() {
}

goToHome() {
    this.router.navigate(['']);
}

goToEmployeeAppointmentPage() {
    this.router.navigate(['employee/appts']);
}
}
