import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  businessId : number;
  constructor(private route : ActivatedRoute) { }

  ngOnInit() {
    this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"))
  }

}
