/*
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ServiceService } from '../../../../services/service/service.service';
import { ServiceDTO } from '../../../../interfaces/service/service-dto';
import { Observable, Subscription } from 'rxjs';

@Component({
    selector: 'app-service-selection',
    templateUrl: './service-selection.component.html',
    styleUrls: ['./service-selection.component.scss']
})
export class ServiceSelectionComponent implements OnInit, OnDestroy {

    services: ServiceDTO[] = [];

    serviceId: number;
    serviceSubscription: Subscription;
    @Input() serviceEvent: Observable<number>;

    @Output() serviceChange = new EventEmitter();

    constructor(private serviceService: ServiceService) {
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
*/
import { Component } from '@angular/core';

    @Component({
        selector: 'app-service-selection',
        templateUrl: './service-selection.component.html',
        styleUrls: ['./service-selection.component.scss']
    })
export class ServiceSelectionComponent {
    title = 'ngSlick';


    slides = [
        {img: "../assets/images/1.jpg"},
        {img: "../assets/images/2.jpg"},
        {img: "../assets/images/3.jpg"},
        {img: "../assets/images/4.jpg"},
        {img: "../assets/images/5.jpg"},
        {img: "../assets/images/6.jpg"},
        {img: "../assets/images/7.jpg"},
        {img: "../assets/images/8.jpg"},
        {img: "../assets/images/9.jpg"},
        {img: "../assets/images/10.jpg"},
        {img: "../assets/images/11.jpg"},
        {img: "../assets/images/12.jpg"}
    ];

    slideConfig = {
        "slidesToShow": 4,
        "slidesToScroll": 1,
        "nextArrow":"<div class='nav-btn next-slide'></div>",
        "prevArrow":"<div class='nav-btn prev-slide'></div>",
        "dots":true,
        "infinite": false
    };

    addSlide() {
        this.slides.push({img: "http://placehold.it/350x150/777777"})
    }

    removeSlide() {
        this.slides.length = this.slides.length - 1;
    }

    slickInit(e) {
        console.log('slick initialized');
    }

    breakpoint(e) {
        console.log('breakpoint');
    }

    afterChange(e) {
        console.log('afterChange');
    }

    beforeChange(e) {
        console.log('beforeChange');
    }


}
