import {Component, OnInit, Input} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BusinessService} from '../../services/business/business.service';
import { GeocodingApiService } from 'src/app/services/google/geocode.service';
import { BusinessDTO } from '../../interfaces/business/business-dto'
import { DomSanitizer} from '@angular/platform-browser';
import { Lightbox, LightboxConfig, LightboxEvent, LIGHTBOX_EVENT, IEvent, IAlbum } from 'ngx-lightbox';
import { Subscription } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';
import { NONE_TYPE } from '@angular/compiler/src/output/output_ast';
import { ImageResizeService } from 'src/app/services/image-resize/image.resize.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
})
export class DetailsComponent implements OnInit {
  dataIsAllLoaded = false;
  public albums: Array<IAlbum> = [];
  businessId : number;
  businessObj : BusinessDTO;
  private _subscription: Subscription;

  constructor( private imageResizeService :ImageResizeService,private spinner: NgxSpinnerService,private _lightbox: Lightbox, private _lightboxEvent: LightboxEvent, private _lighboxConfig: LightboxConfig ,private sanitizer: DomSanitizer, public route: ActivatedRoute, public businessService : BusinessService, private geoCodeService :  GeocodingApiService) {
    // set default config
    this.spinner.show();
    this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"))
    this.businessService.getBusinessById(this.businessId).subscribe(
      res=>{
          this.businessObj = res;
          // this.imagePath = 'data:image/png;base64,' + data["image_encoded"];
          //        // show data base64:
          //        // console.log(this.imagePath);
          //       this.sanitizedImageData = this.sanitizer.bypassSecurityTrustUrl(this.imagePath);
          //     } else {
          //               this.sanitizedImageData = 'assets/images/user_default.png';
          //               console.log(this.sanitizedImageData);
          //           }

          // },
          this.businessService.getMoreInfoBusiness("3040 Rue Sherbrooke Ouest, Montréal, QC H3Z 1A4, Canada", "Dawson College").subscribe(
            res=>{
              for(const pictures_string of res["pictures"]){
                const src : any =  'data:image/jpeg;base64,'+ pictures_string;
                const thumbnial = this.imageResizeService.resizeImage(src, 50,50)
                const album = {
                  src: src,
                  caption: "trying",
                  thumb:thumbnial

               };
               this.albums.push(album);
              }
              
              this.spinner.hide();
              this.dataIsAllLoaded = true;
            }
          )
      }
    )
    this._lighboxConfig.fadeDuration = 1;
  }

  ngOnInit() {
    
  }  


  updateLatLngFromAddress(business: BusinessDTO) {
    let businessArr: string[] = business.formattedAddress.split(',');
    let address = businessArr[0];
    let portalCode = '';
    let selectedPlace = '';
    this.geoCodeService
        .findFromAddress(address, null, null, null, null, null)
        .subscribe(response => {
            if (response.status === 'OK') {
                business.lat = response.results[0].geometry.location.lat;
                business.lng = response.results[0].geometry.location.lng;
            } else if (response.status === 'ZERO_RESULTS') {
                console.log('geocodingAPIService', 'ZERO_RESULTS', response.status);
            } else {
                console.log('geocodingAPIService', 'Other error', response.status);
            }
        });
  }

  open(index: number): void {
    this._subscription = this._lightboxEvent.lightboxEvent$.subscribe((event: IEvent) => this._onReceivedEvent(event));

    // override the default config
    this._lightbox.open(this.albums, index, { wrapAround: true, showImageNumberLabel: true });
  }

  private _onReceivedEvent(event: IEvent): void {
    if (event.id === LIGHTBOX_EVENT.CLOSE) {
      this._subscription.unsubscribe();
    }
  }

  resizeImage(base64image : string) : string{
    return this.imageResizeService.imageToDataUri(base64image, 50,50);
  }
}

