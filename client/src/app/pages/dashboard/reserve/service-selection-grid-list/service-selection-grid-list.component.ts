import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ServiceDTO } from '../../../../interfaces/service/service-dto';
import { Observable, Subscription } from 'rxjs';
import { ServiceService } from '../../../../services/service/service.service';

@Component({
    selector: 'app-service-selection-grid-list',
    templateUrl: 'service-selection-grid-list.component.html',
    styleUrls: ['service-selection-grid-list.component.scss']
})
export class ServiceSelectionGridListComponent implements OnInit, OnDestroy{

    services: ServiceDTO[] = [];

    serviceId: number;
    serviceSubscription: Subscription;
    @Input() serviceEvent: Observable<number>;
    @Output() serviceChange = new EventEmitter();

    itemsPerPageOptions: Array<number> = [4, 8, 16, 32, 64];

    componentState: {
        totalItems: number,
        currentPageSize: number;
        currentPage: number;
    };

    constructor(private serviceService: ServiceService) {
        this.componentState = {
            totalItems: 0,
            currentPageSize: 8,
            currentPage: 1,
        };
    }

    ngOnInit() {
        this.getServices();
    }

    ngOnDestroy() {
        this.serviceSubscription.unsubscribe();
    }

    selectService(service: ServiceDTO) {
        this.serviceChange.emit(service);
        this.serviceId = service.id;
        console.log(this.serviceId);
    }

    getServices() {
        this.serviceService.getServices().subscribe(res => {
            this.services = res;
            this.componentState.totalItems = this.services.length;
            console.log(this.services.length);
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
