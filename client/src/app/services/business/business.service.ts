import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BusinessDTO } from '../../interfaces/business/business-dto';
import { Business } from '../../interfaces/business/business';
import { ServiceDTO } from '../../interfaces/service/service-dto';
import { BusinessRegisterDTO } from '../../interfaces/business/business-register-dto';
import { ServiceCreateDto } from 'src/app/interfaces/service/service-create-dto';
import { BusinessUserRegisterDTO } from 'src/app/interfaces/user/business-user-register-dto';
import { BusinessHoursDTO } from 'src/app/interfaces/business/businessHours-dto';
import { HttpParams, HttpClient } from '@angular/common/http';

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
        if(avatar != null){
            return this.http.post<boolean>(['api', 'businesses', 'businessWithLogo'].join('/'), formData);
        }else{
            return this.http.post<boolean>(['api', 'businesses', 'businessNoLogo'].join('/'), formData);
        }
    }
    public getBusinessById(businessId: number): Observable<BusinessDTO> {
        return this.http.get<BusinessDTO>(['api', 'businesses', businessId.toString()].join('/'));
    }

    public getMoreInfoBusiness(formatted_address: string, nameOfBusiness:string): Observable<any> {
        let params = new HttpParams();
        params = params.append('addressOfBusiness', formatted_address);
        params = params.append('nameOfBusiness', nameOfBusiness)
        console.log()
        return this.http.get<any>(['api', 'businesses','getMoreInfo'].join('/'),{params: params} );
    }

    public getAllBusinesses():Observable<BusinessDTO[]>{
        return this.http.get<BusinessDTO[]>(['api', 'businesses',''].join('/'));
    }

    public findBusinessThroughGoogle(businessName : string):Observable<BusinessDTO[]>{
        let params = new HttpParams();
        params = params.append('nameOfBusiness', businessName);
        return this.http.get<BusinessDTO[]>(['api','businesses','findWithGoogle'].join('/'),{params: params} );
    }

}
