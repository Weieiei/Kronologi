import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { UserService } from './services/user/user.service';

@Injectable()
export class UrlInterceptor implements HttpInterceptor {

    constructor(private injector: Injector) { }

    /**
     * This function will be called for all HTTP calls.
     */
    intercept(
        request: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {

        const userService = this.injector.get(UserService);

        // Forward all api/ paths to server path.
        if (request.url.startsWith('api/')) {
            const url = environment.baseUrl;
            request = request.clone({
                url: url + request.url,
                setHeaders: {
                    Authorization: `Bearer ${userService.getToken()}`
                }
            });
        }

        return next.handle(request);

    }
}
