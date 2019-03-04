import { Component, OnInit, Input, EventEmitter, Output  } from '@angular/core';
import {ThemeService} from "../../core/theme/theme.service";

@Component({
  selector: 'app-cards-ui',
  templateUrl: './cards-ui.component.html',
  styleUrls: ['./cards-ui.component.scss']
})
export class CardsUiComponent implements OnInit {

  @Input() public businessName : String;
  @Input() public buttonName1: String;
  @Input() public buttonName2: String;
  @Input() public image: String;
  @Output() public button1FunctionMapping : EventEmitter<any> = new EventEmitter<any>();
  @Output() public button2FunctionMapping : EventEmitter<any> = new EventEmitter<any>();
  constructor(public themeService: ThemeService) { }
    darkModeActive: boolean;
  ngOnInit() {
      this.themeService.darkModeState.subscribe( value => {
          this.darkModeActive = value;
      })
  }

  button1Function(){
    this.button1FunctionMapping.emit();
  }

  button2Function(){
    this.button2FunctionMapping.emit();
  }

}
