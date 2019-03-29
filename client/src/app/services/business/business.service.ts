import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BusinessDTO } from '../../interfaces/business/business-dto';
import { Business } from '../../interfaces/business/business';
import { ServiceDTO } from '../../interfaces/service/service-dto';
import { BusinessRegisterDTO } from '../../interfaces/business/business-register-dto';
import { ServiceCreateDto } from 'src/app/interfaces/service/service-create-dto';
import { BusinessUserRegisterDTO } from 'src/app/interfaces/user/business-user-register-dto';
import { BusinessHoursDTO } from 'src/app/interfaces/business/businessHours-dto';

@Injectable({
    providedIn: 'root'
})
export class BusinessService {

    constructor(private http: HttpClient) {
    }

    public createBusiness(business: BusinessRegisterDTO, service : ServiceCreateDto, newUser : BusinessUserRegisterDTO, businessHoursDTO: BusinessHoursDTO[], avatar : File): Observable<boolean> {
        let formData = new FormData();

        formData.append('file',avatar);
        formData.append('business', new Blob([JSON.stringify(business)],
            {
                type: "application/json",
            }));    
        formData.append('service', new Blob([JSON.stringify(service)],
            {
                type: "application/json",
            }));

        formData.append('user', new Blob([JSON.stringify(newUser)],
            {
                type: "application/json",
            }));
        
        formData.append('businessHour', new Blob([JSON.stringify(businessHoursDTO)],
            {
                type: "application/json",
            }));

        return this.http.post<boolean>(['api', 'businesses', 'business'].join('/'), formData);
    }
    public getBusinessById(businessId: number): Observable<BusinessDTO> {
        return this.http.get<BusinessDTO>(['api', 'businesses', businessId.toString()].join('/'));
    }

    public getAllBusinesses():Observable<BusinessDTO[]>{
        return this.http.get<BusinessDTO[]>(['api', 'businesses',''].join('/'));
    }

}
