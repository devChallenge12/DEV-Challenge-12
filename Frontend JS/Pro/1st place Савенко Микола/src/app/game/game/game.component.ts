import {AfterViewInit, Component, ElementRef, OnInit, ViewEncapsulation} from '@angular/core';
import {GameRendererService} from '../game.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class GameComponent implements OnInit, AfterViewInit {

  gameRendererService;

  constructor(private elRef: ElementRef) {
  }

  ngOnInit() {
    this.gameRendererService = new GameRendererService(this.elRef.nativeElement.querySelector('.game'));
    this.gameRendererService.init();
  }

  ngAfterViewInit() {
  }

}
