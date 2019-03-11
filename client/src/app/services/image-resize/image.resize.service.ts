import {ElementRef, Injectable, ViewChild} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ImageResizeService {

    @ViewChild('myCanvas') myCanvas: ElementRef;
    public context: CanvasRenderingContext2D;
  constructor() { }

  resizeImage(img, width, height) {
      this.context = (<HTMLCanvasElement>this.myCanvas.nativeElement).getContext('2d');
      this.context.drawImage(img, 0, 0, width, height);
      return this.context.canvas.toDataURL();
  }
}


