import { Component, OnInit } from '@angular/core';
import { Service } from '../../../../models/service/Service';
import { ServiceService } from "../../../../services/service/service.service";
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
        console.log('Hi');
        this.getAllServices();
    }

    getAllServices(): void {
        this.serviceService.getServices().pipe(
            map(data => {
                let i = 0;
                return data.map(a => {
                    console.log(a);
                    return a;
                });
            })
        ).subscribe(
            res => this.services = res,
            err => console.log(err)
        );
    }

}
