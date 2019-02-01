import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ServiceService } from '../../../../services/service/service.service';

@Component({
    selector: 'app-service-selection',
    templateUrl: './service-selection.component.html',
    styleUrls: ['./service-selection.component.scss']
})
export class ServiceSelectionComponent implements OnInit {

    services = [];

    @Input() serviceId?: number;

    @Output() serviceChange = new EventEmitter();

    constructor(private serviceService: ServiceService) {
    }

    ngOnInit() {
        this.getServices();
    }

    selectService(service: any) {
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
