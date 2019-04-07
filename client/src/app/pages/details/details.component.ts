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
  public lng;
  public lat;  

  constructor( private imageResizeService :ImageResizeService,private spinner: NgxSpinnerService,private _lightbox: Lightbox, private _lightboxEvent: LightboxEvent, private _lighboxConfig: LightboxConfig ,private sanitizer: DomSanitizer, public route: ActivatedRoute, public businessService : BusinessService, private geoCodeService :  GeocodingApiService) {
    // set default config
    this.spinner.show();
    this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"))
    this.businessService.getBusinessById(this.businessId).subscribe(
      res=>{
          this.businessObj = res;

          this.businessService.getMoreInfoBusiness("3040 Rue Sherbrooke Ouest, Montréal, QC H3Z 1A4, Canada", "Dawson College").subscribe(
            async res=>{
              console.log(res)
              for(const pictures_string of res["pictures"]){
                const src : any =  'data:image/jpeg;base64,'+ pictures_string; 
                const thumbnail = await this.resizedataURL(src,150,150); 
                const album = {
                  src: src,
                  caption: "trying",
                  thumb:thumbnail.toString()

               };
               this.albums.push(album);
              }
              
              this.updateLatLngFromAddress("3040 Rue Sherbrooke Ouest, Montréal, QC H3Z 1A4, Canada")
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


  updateLatLngFromAddress(formattedAddress:string) {
    let businessArr: string[] =  formattedAddress.split(',');
    let address = businessArr[0];
    let portalCode = '';
    let selectedPlace = '';
    this.geoCodeService
        .findFromAddress(address, null, null, null, null, null)
        .subscribe(response => {
            if (response.status === 'OK') {
                this.lat = response.results[0].geometry.location.lat;
                this.lng = response.results[0].geometry.location.lng;
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

  resizedataURL(datas, wantedWidth, wantedHeight){
    return new Promise(async function(resolve,reject){

        // We create an image to receive the Data URI
        var img = document.createElement('img');

        // When the event "onload" is triggered we can resize the image.
        img.onload = function()
        {        
            // We create a canvas and get its context.
            var canvas = document.createElement('canvas');
            var ctx = canvas.getContext('2d');

            // We set the dimensions at the wanted size.
            canvas.width = wantedWidth;
            canvas.height = wantedHeight;

            // We resize the image with the canvas method drawImage();
            ctx.drawImage(img, 0, 0, wantedWidth, wantedHeight);

            var dataURI = canvas.toDataURL();

            // This is the return of the Promise
            resolve(dataURI);
        };

        // We put the Data URI in the image's src attribute
        img.src = datas;

    })
}
}

