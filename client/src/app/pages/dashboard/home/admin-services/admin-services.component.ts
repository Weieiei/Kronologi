import { Component, OnInit } from '@angular/core';
import { Service } from '../../../../models/service/Service';
import { ServiceService } from '../../../../services/service/service.service';
import { map } from 'rxjs/operators';
import { UserToDisplay } from '../../../../models/user/UserToDisplay';

@Component({
    selector: 'app-admin-services',
    templateUrl: './admin-services.component.html',
    styleUrls: ['./admin-services.component.scss']
})
export class AdminServicesComponent implements OnInit {

    displayedColumns: string[] = ['id', 'name', 'duration', 'client', 'employee'];
    services: Service[];

    componentState: {
        services: Array<Service>,
        // currentSort: IDataTableSort,
        currentPage: number,
        itemsPerPage: number,
        search: string,
        totalItems: number,
    };

    constructor(private serviceService: ServiceService) {
    }

    ngOnInit() {
        this.componentState = {
            services: [],
            // currentSort: IDataTableSort,
            currentPage: 1,
            itemsPerPage: 10,
            search: '',
            totalItems: 0,
        };

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
            res => {
                this.services = res;
                this.componentState.services = res;
                this.updateServices(this.services);
            },
            err => console.log(err)
        );
    }

    filterItems(items: Array<Service>, search: string): Array<Service> {
        if (search.length < 1) {
            return items;
        }

        const searchCriteria = search.toLowerCase();
        return items.filter((item) => item.name.toLowerCase().includes(searchCriteria));
    }

    updateServices(allItems: Array<Service>): void {
        if (allItems) {
            const filteredItems = this.filterItems(allItems, this.componentState.search);
            const totalItems = filteredItems.length;
            this.componentState.services = filteredItems;
            this.componentState.totalItems = totalItems;
        }
    }


    onSearch(searchTerm: string) {
        this.componentState.currentPage = 1;
        this.componentState.search = searchTerm;
        this.updateServices(this.services);
    }

}
