import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ServiceService } from '../../../../services/service/service.service';
import { ServiceDTO } from '../../../../interfaces/service/service-dto';
import { Observable, Subscription } from 'rxjs';
//require('materialize-css/sass/materialize.scss');

@Component({
    selector: 'app-service-selection',
    templateUrl: './service-selection.component.html',
    styleUrls: ['./service-selection.component.scss']
})
export class ServiceSelectionComponent implements OnInit, OnDestroy {

    services: ServiceDTO[] = [];
    items: Array<any> = [];

    serviceId: number;
    serviceSubscription: Subscription;
    @Input() serviceEvent: Observable<number>;

    @Output() serviceChange = new EventEmitter();
    slideConfig = {"slidesToShow": 4, "slidesToScroll": 1};
    constructor(private serviceService: ServiceService) {
        this.items=[
            {name : 'assets/images/alex-bertha-215867-unsplash.jpg'},
            {name : 'assets/images/anshu-a-1147827-unsplash.jpg'},
           // {name : 'assets/images/deniz-altindas-38128-unsplash.jpg'}
        ];


    }

    ngOnInit() {
        this.getServices();
        this.serviceSubscription = this.serviceEvent.subscribe(res => this.serviceId = res);
    }

    ngOnDestroy() {
        this.serviceSubscription.unsubscribe();
    }

    selectService(service: ServiceDTO) {
        this.serviceChange.emit(service);
    }

    getServices() {
        this.serviceService.getServices().subscribe(res => {
            this.services = res;
            this.services.sort((a, b) => {
                if (a.name.toLowerCase() < b.name.toLowerCase()) {
                    return -1;
                } else if (a.name.toLowerCase() > b.name.toLowerCase()) {
                    return 1;
                }
                return 0;
            });
        });
    }
}
/*
import { Component } from '@angular/core';

@Component({
    selector: 'app-service-selection',
    templateUrl: './service-selection.component.html',
    styleUrls: ['./service-selection.component.scss']
})
export class ServiceSelectionComponent {
    items: Array<any> = [];

constructor(){
    this.items = [
        {name : 'assets/images/alex-bertha-215867-unsplash.jpg'},
        {name : 'client/src/assets/images/anshu-a-1147827-unsplash.jpg'},
        {name : 'client/src/assets/images/deniz-altindas-38128-unsplash.jpg'},
        {name : 'assets/images/alex-bertha-215867-unsplash.jpg'},
        {name : 'client/src/assets/images/toa-heftiba-578093-unsplash.jpg'},
    ];
}
}
*/
