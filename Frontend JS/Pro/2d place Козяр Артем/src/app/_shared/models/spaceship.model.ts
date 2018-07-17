import {SpacesbodyModel} from './spacesbody.model';

export class SpaceshipModel extends SpacesbodyModel {
  public speedBack = 1;
  public isGoBack = false;
  public isFire = false;
  public fireReloadMax = 10;
  public fireReloadCurrent = 0;

  constructor(data?: any) {
    super(data);
    this.radius = 22;
    this.speed = 3;
  }

  public go(isBack = false) {
    this.isMove = !isBack;
    this.isGoBack = isBack;
  }

  public stop() {
    this.isMove = false;
    this.isGoBack = false;
  }

  public move() {
    if (this.isMove) {
      this.position.x += Math.sin(this.angle) * this.speed;
      this.position.y -= Math.cos(this.angle) * this.speed;
    } else if (this.isGoBack) {
      this.position.x -= Math.sin(this.angle) * this.speedBack;
      this.position.y += Math.cos(this.angle) * this.speedBack;
    }
  }

  public isFireble(): boolean {
    return this.isFire && this.fireReloadCurrent >= this.fireReloadMax;
  }

  public fireReload() {
    this.fireReloadCurrent = 0;
  }

  public fire() {
    this.fireReloadCurrent++;
  }
}
