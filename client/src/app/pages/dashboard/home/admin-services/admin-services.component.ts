import { Component, OnInit } from '@angular/core';
import { Service } from '../../../../models/service/Service';
import { ServiceService } from '../../../../services/service/service.service';
import { map } from 'rxjs/operators';
import { UserToDisplay } from '../../../../models/user/UserToDisplay';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-admin-services',
    templateUrl: './admin-services.component.html',
    styleUrls: ['./admin-services.component.scss']
})
export class AdminServicesComponent implements OnInit {
 // Fields to upload a service profile picture
 selectedFile: File = null;
 fileSelectMsg: string = 'No file selected yet.';
 fileUploadMsg: string = 'No file uploaded yet.';
    serviceUploadId: number;

    displayedColumns: string[] = ['id', 'name', 'duration', 'client', 'employee', 'profile'];
    services: Service[];

    componentState: {
        services: Array<Service>,
        // currentSort: IDataTableSort,
        currentPage: number,
        itemsPerPage: number,
        search: string,
        totalItems: number,
    };

    constructor(private serviceService: ServiceService,
        private router: Router
        ) {
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

      updateServiceProfilePicture(serviceId): void {
          console.log(serviceId);
        if (this.selectedFile != null) {
            this.serviceService.updateServicePicture(this.selectedFile , serviceId.value).subscribe(
                res => {
                    console.log('File seccessfully uploaded. ');
                    //this.fileUploadMsg = 'File seccessfully uploaded. ';
                    //get picture and show it in the profile  or update the page
                    this.router.navigate(['business']);


                },
                err => {
                    if (err instanceof HttpErrorResponse) {
                        err => console.log(err)
                    }
                }
            );
        }
    }
}
