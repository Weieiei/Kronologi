import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-google-maps',
  templateUrl: './google-maps.component.html',
  styleUrls: ['./google-maps.component.scss'],
  styles: [`
  agm-map {
    height: 500px;
    width:  540px;
  }
`]
})
export class GoogleMapsComponent implements OnInit {
  mapType = 'roadmap';
  @Input() lat;
  @Input() lng;
  zoom = 16
  
  constructor() { }

  ngOnInit() {
  }

}
