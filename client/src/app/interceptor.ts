import { Injectable } from "@angular/core";
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpResponse, HttpErrorResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment.prod";

@Injectable()
export class UrlInterceptor implements HttpInterceptor {

    constructor() { }

    /**
     * This function will be called for all HTTP calls.
     */
    intercept(
        request: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        
        // Forward all api/ paths to server path.
        if (request.url.startsWith('api/')) {
            const url = environment.baseUrl;
            request = request.clone({
                url: url + request.url
            })
        }

        return next.handle(request);
        
    }
}
