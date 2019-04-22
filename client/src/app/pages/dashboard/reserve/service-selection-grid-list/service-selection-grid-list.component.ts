import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ServiceDTO} from '../../../../interfaces/service/service-dto';
import {Observable, Subscription} from 'rxjs';
import {ServiceService} from '../../../../services/service/service.service';
import {DomSanitizer} from '@angular/platform-browser';
import {AppointmentService} from "../../../../services/appointment/appointment.service";

@Component({
    selector: 'app-service-selection-grid-list',
    templateUrl: 'service-selection-grid-list.component.html',
    styleUrls: ['service-selection-grid-list.component.scss']
})
export class ServiceSelectionGridListComponent implements OnInit {

    services: ServiceDTO[] = [];

    imagePath: string = '';
    servicesAvailable: number[] = [];


    serviceId: number;
    serviceSubscription: Subscription;
    @Input() serviceEvent: Observable<number>;
    @Input() businessId: number;
    @Output() serviceChange = new EventEmitter();

    itemsPerPageOptions: Array<number> = [4, 8, 16, 32, 64];
    sanitizedImageData: any;

    componentState: {
        totalItems: number,
        currentPageSize: number;
        currentPage: number;
    };


    constructor(private serviceService: ServiceService,
      private sanitizer: DomSanitizer,
                private appointmentService: AppointmentService) {

        this.componentState = {
            totalItems: 0,
            currentPageSize: 8,
            currentPage: 1,
        };
    }

    ngOnInit() {
       // this.getServices();
        console.log( this.getServices());
    }

    selectService(service: ServiceDTO) {
        this.serviceChange.emit(service);
        this.serviceId = service.id;
    }

    getServices() {
        this.serviceService.getServices(this.businessId).subscribe(res => {
            this.services = res;

            for (let service in this.services) {
                let serviceId = this.services[service]['id'];
                this.appointmentService.getAvailabilitiesForService(this.businessId, serviceId).subscribe(
                    res => {
                        if (res.toString().length != 0)
                            this.servicesAvailable.push(serviceId);
                    }
                )
            }
            for (let service of this.services) {
                this.serviceService.getServiceProfile(this.businessId, service.id).subscribe(
                    data => {
                        if (data) {
                            this.imagePath = 'data:image/png;base64,' + data["image_encoded"];
                            this.sanitizedImageData = this.sanitizer.bypassSecurityTrustUrl(this.imagePath);
                        } else {
                            this.sanitizedImageData = 'assets/images/kronologi-logo-1.png';
                        }

                    },
                    err => console.log(err)
                );
            }

            //added pics
            this.componentState.totalItems = this.services.length;
            this.services.sort((a, b) => {
                if (a.name.toLowerCase() < b.name.toLowerCase()) {
                    return -1;
                } else if (a.name.toLowerCase() > b.name.toLowerCase()) {
                    return 1;
                }
                return 0;
            });
            this.services = this.services.slice(this.getCurrentPageStartIndex(), this.getCurrentPageStartIndex()
                + this.componentState.currentPageSize);
        });
    }

    getCurrentPageStartIndex(): number {
        return (this.componentState.currentPage - 1) * this.componentState.currentPageSize;
    }

    printPageEvent($event) {
        if ($event.pageSize !== this.componentState.currentPageSize) {
            this.componentState.currentPage = 1;
            this.componentState.currentPageSize = $event.pageSize;
            this.getServices();
        } else if ($event.pageIndex !== this.componentState.currentPage + 1) {
            this.componentState.currentPage = $event.pageIndex + 1;
            this.getServices();
        }
    }
}
