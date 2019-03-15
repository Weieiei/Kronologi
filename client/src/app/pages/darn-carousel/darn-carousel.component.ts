import { Component, OnInit } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { SwiperModule } from 'angular2-useful-swiper';
import { NgModule } from '@angular/core';


@Component({
  selector: 'darn-carousel',
  templateUrl: './darn-carousel.component.html',
  styleUrls: ['./darn-carousel.component.scss']
})
export class DarnCarouselComponent {
    title = 'NG7Swiper';


    config: SwiperOptions = {
        autoplay: 3000, // Autoplay option having value in milliseconds
        initialSlide: 3, // Slide Index Starting from 0
        slidesPerView: 3, // Slides Visible in Single View Default is 1
        pagination: '.swiper-pagination', // Pagination Class defined
        paginationClickable: true, // Making pagination dots clicable
        nextButton: '.swiper-button-next', // Class for next button
        prevButton: '.swiper-button-prev', // Class for prev button
        spaceBetween: 30 // Space between each Item
    };
}
