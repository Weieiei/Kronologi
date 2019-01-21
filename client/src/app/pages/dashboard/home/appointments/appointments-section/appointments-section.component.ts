import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'app-appointments-section',
    templateUrl: './appointments-section.component.html',
    styleUrls: ['./appointments-section.component.scss']
})
export class AppointmentsSectionComponent implements OnInit {

    @Input() appointments = [];
    @Input() title: string;

    constructor() {
    }

    ngOnInit() {
    }

}
