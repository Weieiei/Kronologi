import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { animate, state, style, transition, trigger } from '@angular/animations';

type PaneType = 'left' | 'right';

@Component({
  selector: 'app-slide-panel',
  templateUrl: './slide-panel.component.html',
  styleUrls: ['./slide-panel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [
    trigger('slide', [
      state('left', style({ transform: 'translateX(0)' })),
      state('right', style({ transform: 'translateX(-50%)' })),
      transition('* => *', animate(250))
    ])]
})
export class SlidePanelComponent implements OnInit {

  @Input() activePane: PaneType = 'left';

  constructor() { }

  ngOnInit() {
  }

  goLeft() {
    this.activePane = 'left';
  }

  goRight() {
    this.activePane = 'right';
  }

}
