import { Component, OnInit } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { SwiperModule } from 'angular2-useful-swiper';
import { NgModule } from '@angular/core';


@Component({
  selector: 'darn-carousel',
  templateUrl: './darn-carousel.component.html',
  styleUrls: ['./darn-carousel.component.scss']
})
export class DarnCarouselComponent  implements OnInit {
    slides = [
        {img: "assets/images/alex-bertha-215867-unsplash.jpg"},
        {img: "assets/images/anshu-a-1147827-unsplash.jpg"},
    ];
    slideConfig = {"slidesToShow": 1, "slidesToScroll": 1};

    addSlide() {
        this.slides.push({img: "assets/images/alex-bertha-215867-unsplash.jpg"})
        this.slides.push({img: "assets/images/anshu-a-1147827-unsplash.jpg"})
    }

    removeSlide() {
        this.slides.length = this.slides.length - 1;
    }

    slickInit(e) {
        console.log('slick initialized');
    }

    breakpoint(e) {
        console.log('breakpoint');
    }

    afterChange(e) {
        console.log('afterChange');
    }

    beforeChange(e) {
        console.log('beforeChange');
    }

    ngOnInit(): void {
    }
}
