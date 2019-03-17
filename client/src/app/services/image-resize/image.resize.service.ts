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
    private businessImgWidth = 200;
    private userImgHeight = 100;
    private userImgWidth = 100;

    // ratios between height and width:
    private businessImageRatio = this.businessImgHeight/this.businessImgWidth;
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

    // to properly crop the image, we calculate the ratio
    // this allows us to crop either the top and bottom or the left and right parts of the image
    // we crop first and then resize to avoid stretching the image the user provides us

    businessImgResize(img) {
        const context: CanvasRenderingContext2D = (<HTMLCanvasElement>this.myCanvas.nativeElement).getContext('2d');
        // getting the actual dimensions of the uploaded image and comparing to the expected ratio
        if ((img.naturalHeight / img.naturalWidth) > this.businessImageRatio) { // if the image is taller than expected
            // crop the top and bottom:
            const destinationHeight = img.naturalHeight - (img.naturalWidth * this.businessImageRatio);
            context.drawImage(img, 0, (img.naturalHeight - destinationHeight) / 2, img.naturalWidth, destinationHeight, 0, 0, img.naturalWidth, destinationHeight);

        }
        else if ((img.naturalHeight / img.naturalWidth) < this.businessImageRatio) { // if the image is wider than expected
            // crop the right and left:
            const destinationWidth = img.naturalWidth - (img.naturalHeight / this.businessImageRatio);
            context.drawImage(img, (img.naturalWidth - destinationWidth) / 2, 0, destinationWidth, img.naturalHeight, 0, 0, destinationWidth, img.naturalWidth);
        }
        // else, the ratios are equal and there is no need to crop, so we go straight to resizing
        this.resizeImage(img, this.businessImgWidth, this.businessImgHeight);
    }

    userImgResize(img){
      // same logic as method above, we are just using the values for userImg
        const context: CanvasRenderingContext2D = (<HTMLCanvasElement>this.myCanvas.nativeElement).getContext('2d');
        if ((img.naturalHeight / img.naturalWidth) > this.userImageRatio) { // if the image is taller than expected
            // crop the top and bottom:
            const destinationHeight = img.naturalHeight - (img.naturalWidth * this.userImageRatio);
            context.drawImage(img, 0, (img.naturalHeight - destinationHeight) / 2, img.naturalWidth, destinationHeight, 0, 0, img.naturalWidth, destinationHeight);

        }
        else if ((img.naturalHeight / img.naturalWidth) < this.userImageRatio) { // if the image is wider than expected
            // crop the right and left:
            const destinationWidth = img.naturalWidth - (img.naturalHeight / this.userImageRatio);
            context.drawImage(img, (img.naturalWidth - destinationWidth) / 2, 0, destinationWidth, img.naturalHeight, 0, 0, destinationWidth, img.naturalWidth);
        }
        // else, the ratios are equal and there is no need to crop, so we go straight to resizing
        this.resizeImage(img, this.userImgWidth, this.userImgHeight);
    }

}


