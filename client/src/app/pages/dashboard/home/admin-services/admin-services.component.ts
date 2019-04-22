import { Component, OnInit } from '@angular/core';
import { Service } from '../../../../models/service/Service';
import { ServiceService } from '../../../../services/service/service.service';
import { skip, take, mergeMap, switchMap, mapTo } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { merge } from 'rxjs/observable/merge';
import { Observable, Subject } from 'rxjs';

@Component({
    selector: 'app-admin-services',
    templateUrl: './admin-services.component.html',
    styleUrls: ['./admin-services.component.scss']
})
export class AdminServicesComponent implements OnInit {
 // Fields to upload a service profile picture
 businessId : any;
    selectedFile: File = null;
    fileSelectMsg = 'No file selected yet.';
    fileUploadMsg = 'No file uploaded yet.';
    serviceUploadId: number;

    displayedColumns: string[] = ['id', 'name', 'duration', 'profile'];
    services: Service[];

    services$: Observable<Array<Service>>;
    showNotification$: Observable<boolean>;
    update$ = new Subject<void>();
    forceReload$ = new Subject<void>();

    componentState: {
        services: Array<Service>,
        currentPage: number,
        itemsPerPage: number,
        search: string,
        totalItems: number,
    };

    constructor(private serviceService: ServiceService,
                private router: Router, private route: ActivatedRoute
        ) {
    }

    ngOnInit() {
        this.businessId = parseInt(this.route.snapshot.paramMap.get('businessId'));
        this.serviceService.setBusinessId(this.businessId);
        const initialService$ = this.getDataOnce();

        const updates$ = merge(this.update$, this.forceReload$).pipe(
            mergeMap(() => this.getDataOnce())
        );

        this.componentState = {
            services: [],
            currentPage: 1,
            itemsPerPage: 10,
            search: '',
            totalItems: 0,
        };

        this.services$ = merge(initialService$, updates$);

        const reload$ = this.forceReload$.pipe(switchMap(() => this.getNotifications()));
        const initialNotifications$ = this.getNotifications();
        const show$ = merge(initialNotifications$, reload$).pipe(mapTo(true));
        const hide$ = this.update$.pipe(mapTo(false));
        this.showNotification$ = merge(show$, hide$);

        this.getAllServices();
    }

    getDataOnce() {
        return this.serviceService.allServices.pipe(take(1));
    }

    getNotifications() {
        return this.serviceService.allServices.pipe(skip(1));
    }

    forceReload() {
        this.serviceService.forceReload();
        this.forceReload$.next();
    }

    getAllServices(): void {
        this.services$.subscribe(
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

    selectEvent(file: File): void {
        this.selectedFile = file;
        this.fileSelectMsg = file.name;
      }

       uploadEvent(file: File): void {
        this.fileUploadMsg = file.name;
      }

       cancelEvent(): void {
        this.fileSelectMsg = 'No file selected yet.';
        this.fileUploadMsg = 'No file uploaded yet.';
      }

    updateServicePicture(serviceId): void {
        if (this.selectedFile != null) {
            this.serviceService.updateServicePicture(this.selectedFile , serviceId).subscribe(
                res => {
                    console.log('File seccessfully uploaded. ');
                    // get picture and show it in the profile  or update the page
                    this.router.navigate(['business']);

                },
                err => {
                    console.log(err);
                }
            );
        }
    }

    redirectToCreateNewService() {
        this.router.navigate([this.businessId + '/admin/services/create']);
    }
}
