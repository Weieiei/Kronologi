import {Component, ViewChild} from '@angular/core';
import {MatSidenav} from '@angular/material/sidenav';

/** @title Sidenav with custom escape and backdrop click behavior */
@Component({
  selector: 'app-sidenavbar',
  templateUrl: 'sidenavbar.component.html',
  styleUrls: ['sidenavbar.component.scss'],
})
export class SidenavbarComponent {
  @ViewChild('sidenav') sidenav: MatSidenav;

  reason = '';

  close(reason: string) {
    this.reason = reason;
    this.sidenav.close();
  }

  
}
