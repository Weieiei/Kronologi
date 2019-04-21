import { EventEmitter, Component, OnInit, Input, Output } from '@angular/core';
import { ServiceDTO } from 'src/app/interfaces/service/service-dto';

@Component({
  selector: 'app-confirmation-and-payment',
  templateUrl: './confirmation-and-payment.component.html',
  styleUrls: ['./confirmation-and-payment.component.scss']
})
export class ConfirmationAndPaymentComponent implements OnInit {
  @Input() chosenDate: Date;
  @Input() service: ServiceDTO;
  @Input() chosenTime: any;
  @Output() isPaymentWanted  = new EventEmitter();
  public checked : boolean = false;
  constructor() { }

  ngOnInit() {
  }

  onChange(){
    console.log(this.checked)
    this.isPaymentWanted.emit(this.checked)
  }
}
