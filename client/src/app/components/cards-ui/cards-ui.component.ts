import { Component, OnInit, Input, EventEmitter, Output  } from '@angular/core';
import {ThemeService} from "../../core/theme/theme.service";
import { BusinessDTO } from 'src/app/interfaces/business/business-dto';

@Component({
  selector: 'app-cards-ui',
  templateUrl: './cards-ui.component.html',
  styleUrls: ['./cards-ui.component.scss']
})
export class CardsUiComponent implements OnInit {
  
  public url : String;
  public businessDescription : String;
  public businessName : String;

  @Input() businessDTO: BusinessDTO;
  @Input() public buttonName1: String;
  @Input() public buttonName2: String;
  @Input() public image: String;
  @Output() public button1FunctionMapping : EventEmitter<any> = new EventEmitter<any>();
  @Output() public button2FunctionMapping : EventEmitter<any> = new EventEmitter<any>();


  constructor(public themeService: ThemeService) { 
  }
    darkModeActive: boolean;
  ngOnInit() {
      this.themeService.darkModeState.subscribe( value => {
          this.darkModeActive = value;
      })
      console.log(this.businessDTO);
      this.url = this.businessDTO.image;
      if(!this.url){
        this.url = "../../../assets/images/business.jpg"
      }
      this.businessName = this.businessDTO.name
      this.businessDescription = this.businessDTO.description;
  }

  button1Function(){
    this.button1FunctionMapping.emit();
  }

  button2Function(){
    this.button2FunctionMapping.emit();
  }

}
