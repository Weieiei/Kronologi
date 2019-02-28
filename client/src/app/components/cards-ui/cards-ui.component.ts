import { Component, OnInit, Input, EventEmitter, Output  } from '@angular/core';

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
  constructor() { }

  ngOnInit() {
  }

  button1Function(){
    console.log("hello")
    this.button1FunctionMapping.emit();
  }

  button2Function(){
    this.button2FunctionMapping.emit();
  }

}
