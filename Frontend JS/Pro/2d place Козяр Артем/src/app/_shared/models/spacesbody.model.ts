import {PositionModel} from './position.model';

export class SpacesbodyModel {
  public position: PositionModel;
  public speed: number;

  public color: string;

  public radius: number;

  public turnSpeed: number;
  public angle: number;

  public isMove: boolean;
  public isRotatingLeft: boolean;
  public isRotatingRight: boolean;

  public isCrashed = false;

  constructor(data?: any) {
    this.position = new PositionModel(data.position || {x: 0, y: 0});
    this.speed = data.speed || 1;
    this.color = data.color || 'white';
    this.radius = data.radius || 32;
    this.turnSpeed = data.turnSpeed || Math.PI / 180;
    this.angle = data.angle || Math.PI / 2;
    this.isMove = !!data.isMove;
    this.isRotatingLeft = !!data.isRotatingLeft;
    this.isRotatingRight = !!data.isRotatingRight;
  }

  public draw() {
    this.rotare();
    this.move();
  }

  public turn(isLeft = false, isStart = false) {
    if (isLeft) {
      this.isRotatingLeft = isStart;
    } else {
      this.isRotatingRight = isStart;
    }
  }

  public go(isBack = false) {
    this.isMove = !isBack;
  }

  public stop() {
    this.isMove = false;
  }

  public rotare() {
    if (this.isRotatingRight) {
      this.angle += this.turnSpeed;

    } else if (this.isRotatingLeft) {
      this.angle -= this.turnSpeed;
    }
  }

  public move() {
    if (this.isMove) {
      this.position.x += Math.sin(this.angle) * this.speed;
      this.position.y -= Math.cos(this.angle) * this.speed;
    }
  }

  public die() {
    this.isCrashed = true;
  }

  public isHitWith(body: SpacesbodyModel): boolean {
    const w = this.position.x - body.position.x;
    const h = this.position.y - body.position.y;
    const length = Math.sqrt( w * w + h * h );
    return length <= this.radius || length < body.radius;
  }
}
