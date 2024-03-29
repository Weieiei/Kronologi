import { Component, OnInit, Input, EventEmitter, Output  } from '@angular/core';
import {ThemeService} from "../../core/theme/theme.service";
import { BusinessDTO } from 'src/app/interfaces/business/business-dto';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
@Component({
  selector: 'app-cards-ui',
  templateUrl: './cards-ui.component.html',
  styleUrls: ['./cards-ui.component.scss']
})
export class CardsUiComponent implements OnInit {
  
  public url : String;
  public useDefaultPicture : boolean = false;
  public businessDescription : String;
  public businessName : String;
  public bkUrl :any = {};
  @Input() businessDTO: BusinessDTO;
  @Input() public buttonName1: String;
  @Input() public buttonName2: String;
  @Input() public image: String;
  @Output() public button1FunctionMapping : EventEmitter<any> = new EventEmitter<any>();
  @Output() public button2FunctionMapping : EventEmitter<any> = new EventEmitter<any>();
  

  constructor(public themeService: ThemeService, private router: Router, private appointmentService  : AppointmentService) { 
  }
    darkModeActive: boolean;
  ngOnInit() {
      this.themeService.darkModeState.subscribe( value => {
          this.darkModeActive = value;
      })
      this.url = this.businessDTO.image;
      if(!this.url || this.url === undefined){
        this.url = "../../../assets/images/business.jpg";
      }
      this.businessName = this.businessDTO.name
      this.businessDescription = this.businessDTO.description;
      this.bkUrl = this.getBkUrl();
  }

  button1Function(){
    this.router.navigate(['/home/' + this.businessDTO.id] , { state: { businessId: this.businessDTO.id}});
  }

  button2Function(){
    this.router.navigate(['/book/'+ this.businessDTO.id] , { state: { businessId: this.businessDTO.id}});
  }

  moreInfo(){
    this.router.navigate(['/details/' + this.businessDTO.id] , { state: { businessId: this.businessDTO.id}});
  }

  getBkUrl() {
    const styles = {

    'background-image': 'url('+ this.url + ')',
    'background-size': '100% 50%',
     'background-repeat':'no-repeat',
    };
    return styles;
  }

}
