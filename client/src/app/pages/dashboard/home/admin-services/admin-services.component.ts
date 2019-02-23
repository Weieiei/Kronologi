import { Component, OnInit } from '@angular/core';
import { Service } from '../../../../models/service/Service';
import { ServiceService } from '../../../../services/service/service.service';
import { map } from 'rxjs/operators';

@Component({
    selector: 'app-admin-services',
    templateUrl: './admin-services.component.html',
    styleUrls: ['./admin-services.component.scss']
})
export class AdminServicesComponent implements OnInit {

    displayedColumns: string[] = ['id', 'name', 'duration', 'client', 'employee'];
    services: Service[];

    constructor(private serviceService: ServiceService) {
    }

    ngOnInit() {
        this.getAllServices();
    }

    getAllServices(): void {
        this.serviceService.getPlainServices().pipe(
            map(data => {
                return data.map(a => {
                    return a;
                });
            })
        ).subscribe(
            res => this.services = res,
            err => console.log(err)
        );
    }

}
