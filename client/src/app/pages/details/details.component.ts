import {Component, OnInit, Input} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BusinessService} from '../../services/business/business.service';
import { GeocodingApiService } from 'src/app/services/google/geocode.service';
import { BusinessDTO } from '../../interfaces/business/business-dto'
import { Lightbox, LightboxConfig, LightboxEvent, LIGHTBOX_EVENT, IEvent, IAlbum } from 'ngx-lightbox';
import { Subscription } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';
import { ImageResizeService } from 'src/app/services/image-resize/image.resize.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
})
export class DetailsComponent implements OnInit {
  items = Array.from({length: 100000}).map((_, i) => `Item #${i}`);
  dayArray : string[] = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday"];
  expansionPanel : boolean = true;
  dataIsAllLoaded = false;
  businessId : number;
  businessObj : BusinessDTO;
  rating : number;
  private httpSub : Subscription;
  private _subscription: Subscription;
  public lng;
  public lat;  
  public albums: Array<IAlbum> = [];
  public reviews : string[] = [];

  constructor( private imageResizeService :ImageResizeService,private spinner: NgxSpinnerService,private _lightbox: Lightbox, private _lightboxEvent: LightboxEvent, private _lighboxConfig: LightboxConfig , public route: ActivatedRoute, 
                public businessService : BusinessService, private geoCodeService :  GeocodingApiService,private router: Router) {
   
  }

  ngOnInit() {
    this.spinner.show();
    this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"))
    this.httpSub = this.businessService.getBusinessById(this.businessId).subscribe(
      res=>{
          this.businessObj = res;

          if(this.businessObj.business_hours !== undefined){
            this.expansionPanel  = false;
          }
          this.businessService.getMoreInfoBusiness(this.businessObj.formattedAddress, this.businessObj.name).subscribe(
            async res=>{
              
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

              this.rating = res["rating"]
              this.reviews= res["review"]
              this.updateLatLngFromAddress(res["formatted_address"])
              
              this.dataIsAllLoaded = true;
              this.spinner.hide();
            },
            err => {
              console.log("no business found with this name")
              this.lat=0.00000;
              this.lng=0.00000;
            }
          )
      }
    )
    this._lighboxConfig.fadeDuration = 1;
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
  dateToday() : string{
    const today : string = this.dayArray[new Date().getDay()];
    
    if(this.businessObj.business_hours === undefined){
      return "no hours found for this  business"
    }
    for(const businessHours of this.businessObj.business_hours){
      if(businessHours.day === today){
        return "Today: " + businessHours.openHour + " - " + businessHours.closeHour;  
      }
    }

    return "no hours found for today"
  }

  stopDescription(): void{
    this.httpSub.unsubscribe();
    this.spinner.hide();
    this.router.navigate(['business']);
  }
}

