import {ElementRef, Injectable, ViewChild} from '@angular/core';
// pica can be installed by typing npm install pica
// more information is available at https://github.com/nodeca/pica
import pica from 'pica';

@Injectable({
  providedIn: 'root'
})
export class ImageResizeService {

    @ViewChild('myCanvas') myCanvas: ElementRef;
    // public context: CanvasRenderingContext2D;

    //expected image sizes for the business profile image and for the user profile image:
    private businessImgHeight = 250;
    private businessImageWidth = 200;
    private userImgHeight = 100;
    private userImgWidth = 100;

    // ratios between height and width:
    private businessImageRatio = this.businessImgHeight/this.businessImageWidth;
    private userImageRatio = this.userImgHeight/this.userImgWidth;

  constructor() { }

  resizeImage(img, width, height) {
      const resizedCanvas = document.createElement('canvas');
      resizedCanvas.height = height;
      resizedCanvas.width = width;

      pica().resize(img, resizedCanvas, {
              unsharpAmount: 80,
              unsharpRadius: 0.6,
              unsharpThreshold: 2
      }).then(result => {
          console.log(`resize done!  ${result}`);
          return result;})
          .catch(err => console.log(err))

  }

  cropImage (img, width, height) {
      const context: CanvasRenderingContext2D = (<HTMLCanvasElement>this.myCanvas.nativeElement).getContext('2d');
      return context.drawImage(img, 33, 71, 104, 124, 21, 20, 87, 104);
  }

    // to properly crop the image, we calculate the ratio
    // this allows us to crop either the top and bottom or the left and right parts of the image
    // we crop first and then resize to avoid stretching the image the user provides us
    imgResize(img){
      // TO DO: cropping the image before resizing it
        // calculate the ratio and crop the image as needed, the call the resize function

      //img.naturalHeight
    }
}


