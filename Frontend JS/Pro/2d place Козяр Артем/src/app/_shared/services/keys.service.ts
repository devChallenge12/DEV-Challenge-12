import {EventEmitter, Injectable} from '@angular/core';

export const KEY = {
  left: 37,
  right: 39,
  up: 38,
  down: 40,
  space: 32,
  enter: 13,
};

@Injectable()
export class KeysService {

  public $onPressUp: EventEmitter<any> = new EventEmitter();
  public $onPressDown: EventEmitter<any> = new EventEmitter();
  public $onPressLeft: EventEmitter<any> = new EventEmitter();
  public $onPressRight: EventEmitter<any> = new EventEmitter();
  public $onPressEnter: EventEmitter<any> = new EventEmitter();
  public $onPressSpace: EventEmitter<any> = new EventEmitter();

  public $onLetGoUp: EventEmitter<any> = new EventEmitter();
  public $onLetGoDown: EventEmitter<any> = new EventEmitter();
  public $onLetGoLeft: EventEmitter<any> = new EventEmitter();
  public $onLetGoRight: EventEmitter<any> = new EventEmitter();
  public $onLetGoEnter: EventEmitter<any> = new EventEmitter();
  public $onLetGoSpace: EventEmitter<any> = new EventEmitter();

  constructor() {
    document.addEventListener('keydown', this.keyPressed.bind(this));
    document.addEventListener('keyup', this.keyLetGo.bind(this));
  }

  private isOurKey(event): boolean {
    return [37, 38, 39, 40, 32, 13].indexOf(event.keyCode) !== -1;
  }

  private keyPressed(event: any) {
    if (!this.isOurKey(event)) {
      return;
    }
    event.preventDefault();
    switch (event.keyCode) {
      case KEY.left: // Left Arrow key
        this.$onPressLeft.emit();
        break;
      case KEY.right: // Right Arrow key
        this.$onPressRight.emit();
        break;
      case KEY.up: // Up Arrow key
        this.$onPressUp.emit();
        break;
      case KEY.down: // Down Arrow key
        this.$onPressDown.emit();
        break;
      case KEY.enter: // Down Arrow key
        this.$onPressEnter.emit();
        break;
      case KEY.space: // Down Arrow key
        this.$onPressSpace.emit();
        break;
    }
  }

  private keyLetGo(event: any) {
    if (!this.isOurKey(event)) {
      return;
    }
    event.preventDefault();
    switch (event.keyCode) {
      case KEY.left: // Left Arrow key
        this.$onLetGoLeft.emit();
        break;
      case KEY.right: // Right Arrow key
        this.$onLetGoRight.emit();
        break;
      case KEY.up: // Up Arrow key
        this.$onLetGoUp.emit();
        break;
      case KEY.down: // Down Arrow key
        this.$onLetGoDown.emit();
        break;
      case KEY.enter: // Down Arrow key
        this.$onLetGoEnter.emit();
        break;
      case KEY.space: // Down Arrow key
        this.$onLetGoSpace.emit();
        break;
    }
  }

}
