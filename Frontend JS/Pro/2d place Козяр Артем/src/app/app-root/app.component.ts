import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {SpaceshipModel} from '../_shared/models/spaceship.model';
import {KeysService} from '../_shared/services/keys.service';
import {SpacesbodyModel} from '../_shared/models/spacesbody.model';
import {EnemyModel} from '../_shared/models/enemy.model';

const CANVAS_WIDTH = window.innerWidth;
const CANVAS_HEIGHT = window.innerHeight;

const SPACESBODIES = 50;
const ENEMIES = 10;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('shipImg') shipImgEl: ElementRef;
  @ViewChild('strawberryImg') strawberryImg: ElementRef;

  @ViewChild('bombImg') bombImg: ElementRef;

  @ViewChild('canvas') canvasEl: ElementRef;
  public context: CanvasRenderingContext2D;

  public canvas: HTMLCanvasElement;

  public level = 1;
  public score = 0;

  public spaceship: SpaceshipModel;

  public spacesbodies: SpacesbodyModel[] = [];

  public enemies: EnemyModel[] = [];

  public bullets: SpacesbodyModel[] = [];

  constructor(
    private keyService: KeysService,
    private cd: ChangeDetectorRef) {
  }

  ngOnInit() {
    this.keyService.$onPressDown.subscribe(this.engineGoBack.bind(this));
    this.keyService.$onPressUp.subscribe(this.engineOn.bind(this));
    this.keyService.$onPressLeft.subscribe(() => this.turnLeft(true));
    this.keyService.$onPressRight.subscribe(() => this.turnRight(true));
    this.keyService.$onPressEnter.subscribe(this.start.bind(this));
    this.keyService.$onPressSpace.subscribe(this.startFire.bind(this));

    this.keyService.$onLetGoDown.subscribe(this.engineOff.bind(this));
    this.keyService.$onLetGoUp.subscribe(this.engineOff.bind(this));
    this.keyService.$onLetGoLeft.subscribe(() => this.turnLeft());
    this.keyService.$onLetGoRight.subscribe(() => this.turnRight());
    this.keyService.$onLetGoSpace.subscribe(this.stopFire.bind(this));
  }

  ngAfterViewInit(): void {
    this.canvas = <HTMLCanvasElement>this.canvasEl.nativeElement;
    this.canvas.width = CANVAS_WIDTH;
    this.canvas.height = CANVAS_HEIGHT;

    this.context = this.canvas.getContext('2d');

    this.start();
  }

  ngOnDestroy() {
  }

  public start() {
    this.level = 1;
    this.score = 0;
    this.spaceship = new SpaceshipModel({position: {x: 40, y: 200}});
    this.spacesbodies = [];
    this.enemies = [];
    this.bullets = [];
    this.updateLevel(1);

    setInterval(() => {
      this.initSpacesbodies();
      this.initEnemies();
    }, 36000);

    this.draw();

    this.cd.detectChanges();
  }

  private draw() {
    this.go();

    if (this.spaceship.isCrashed) {
      return;
    }

    this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);

    // spaceship
    this.spaceship.draw();
    this.drawSpaceship();

    // spacesbodies
    this.spacesbodies.forEach((spacesbody) => {
      if (spacesbody.isCrashed) {
        return;
      }

      spacesbody.draw();
      if (this.strawberryImg) {
        this.context.save();
        this.context.translate(spacesbody.position.x, spacesbody.position.y);
        this.context.rotate(spacesbody.angle);
        this.context.drawImage(
          this.strawberryImg.nativeElement,
          -spacesbody.radius / 2, -spacesbody.radius / 2,
          spacesbody.radius, spacesbody.radius);
        this.context.restore();
      } else {
        this.drawSpacebody(spacesbody);
      }
    });

    // enemies
    this.enemies.forEach((enemy) => {
      if (enemy.isCrashed) {
        return;
      }

      enemy.draw();
      if (this.bombImg) {
        this.context.save();
        this.context.translate(enemy.position.x, enemy.position.y);
        this.context.rotate(enemy.angle);
        this.context.drawImage(
          this.bombImg.nativeElement,
          -enemy.radius / 2, -enemy.radius / 2,
          enemy.radius, enemy.radius);
        this.context.restore();
      } else {
        this.drawSpacebody(enemy);
      }
    });

    // bullets
    this.bullets.forEach((bullet) => {
      if (bullet.isCrashed) {
        return;
      }
      bullet.draw();
      this.drawSpacebody(bullet);
    });

    requestAnimationFrame(this.draw.bind(this));
  }

  private drawSpaceship() {
    if (this.spaceship.position.x < 0) {
      this.spaceship.position.x = CANVAS_WIDTH;
    }
    if (this.spaceship.position.y < 0) {
      this.spaceship.position.y = CANVAS_HEIGHT;
    }
    if (this.spaceship.position.x > CANVAS_WIDTH) {
      this.spaceship.position.x = 0;
    }
    if (this.spaceship.position.y > CANVAS_HEIGHT) {
      this.spaceship.position.y = 0;
    }

    this.context.save();
    if (this.shipImgEl) {
      this.context.translate(this.spaceship.position.x, this.spaceship.position.y);
      this.context.rotate(this.spaceship.angle);
      this.context.drawImage(
        this.shipImgEl.nativeElement,
        -this.spaceship.radius / 2, -this.spaceship.radius / 2,
        this.spaceship.radius, this.spaceship.radius);

    } else {
      this.drawSpacebody(this.spaceship, true);
    }

    this.drawFlame();
    this.context.restore();
  }

  private drawSpacebody(spacebody: SpacesbodyModel, isSkipContex = false) {
    if (!isSkipContex) {
      this.context.save();
    }
    this.context.beginPath();
    this.context.translate(spacebody.position.x, spacebody.position.y);
    this.context.rotate(spacebody.angle);
    this.context.arc(spacebody.radius * -0.5, spacebody.radius * -0.5, spacebody.radius, 0, 2 * Math.PI);
    this.context.fillStyle = spacebody.color;
    this.context.fill();
    this.context.closePath();

    this.context.stroke();

    if (!isSkipContex) {
      this.context.restore();
    }
  }

  private turnLeft(isStart = false) {
    this.spaceship.turn(true, isStart);
  }

  private turnRight(isStart = false) {
    this.spaceship.turn(false, isStart);
  }

  private engineOn() {
    this.spaceship.go();
  }

  private engineGoBack() {
    this.spaceship.go(true);
  }

  private engineOff() {
    this.spaceship.stop();
  }

  private drawFlame() {
    if (this.spaceship.isMove) {
      this.context.beginPath();
      this.context.moveTo(this.spaceship.radius * -0.5, this.spaceship.radius * 0.5);
      this.context.lineTo(this.spaceship.radius * 0.5, this.spaceship.radius * 0.5);
      this.context.lineTo(0, this.spaceship.radius * 0.5 + Math.random() * 10);
      this.context.lineTo(this.spaceship.radius * -0.5, this.spaceship.radius * 0.5);
      this.context.closePath();
      this.context.fillStyle = 'orange';
      this.context.fill();
    }
  }

  private go() {

    this.fire();

    // enemies
    this.enemies.forEach((enemy) => {
      if (this.spaceship.isHitWith(enemy)) {
        this.spaceship.die();
      }
    });

    // spacesbodies
    this.spacesbodies = this.spacesbodies.filter((spacesbody) => {
      if (this.spaceship.isHitWith(spacesbody)) {
        this.addScore(1);
        spacesbody.die();
        return false;
      }
      return true;
    });

    // bullets
    this.bullets = this.bullets.filter((bullet) => {
      let isHited = false;
      this.enemies = this.enemies.filter((enemy) => {
        if (bullet.isHitWith(enemy)) {
          this.addScore(10);
          isHited = true;
          enemy.die();
          return false;
        }
        return true;
      });
      return !isHited;
    });

    this.cd.detectChanges();
  }

  private initSpacesbodies() {
    for (let i = 0; i <= SPACESBODIES; i++) {
      const rotate = Math.random() < 0.5;
      const body = new SpacesbodyModel({
        position: {
          x: Math.round(Math.random() * CANVAS_WIDTH),
          y: Math.round(Math.random() * CANVAS_HEIGHT)
        },
        speed: Math.round(Math.random() * 10),
        angle: Math.PI * Math.random(),
        color: 'green',
        isMove: Math.random() > 0.1,
        isRotatingLeft: rotate,
        isRotatingRight: !rotate
      });
      if (!this.spaceship.isHitWith(body)) {
        this.spacesbodies.push(body);
      }
    }
  }

  private initEnemies() {
    for (let i = 0; i <= ENEMIES; i++) {
      const rotate = Math.random() < 0.5;
      const body = new EnemyModel({
        position: {
          x: Math.round(Math.random() * CANVAS_WIDTH),
          y: Math.round(Math.random() * CANVAS_HEIGHT)
        },
        speed: Math.round(Math.random() * 10),
        angle: Math.PI * Math.random(),
        isMove: Math.random() > 0.1,
        isRotatingLeft: rotate,
        isRotatingRight: !rotate
      });
      if (!this.spaceship.isHitWith(body)) {
        this.enemies.push(body);
      }
    }
  }

  private startFire() {
    this.spaceship.isFire = true;
  }

  private stopFire() {
    this.spaceship.isFire = false;
  }

  private fire() {
    this.spaceship.fire();
    if (this.spaceship.isFireble()) {
      this.spaceship.fireReload();
      const bullet = new SpacesbodyModel({
        position: {
          x: this.spaceship.position.x,
          y: this.spaceship.position.y
        },
        color: 'orange',
        width: 4,
        height: 4,
        speed: 10,
        radius: 8,
        angle: this.spaceship.angle,
        isMove: true
      });
      switch (this.level) {
        case 5:
          bullet.speed = 50;
          bullet.radius = 32;
          break;
        case 4:
          bullet.speed = 40;
          bullet.radius = 24;
          break;
        case 3:
          bullet.speed = 30;
          bullet.radius = 16;
          break;
        case 2:
          bullet.speed = 20;
          bullet.radius = 12;
          break;
      }
      this.bullets.push(bullet);
    }
  }

  private addScore(score: number) {
    this.score += score;
    const level = this.getLevel(this.score);
    if (this.level !== level) {
      this.updateLevel(level);
    }
  }

  private getLevel(score: number): number {
    if (score < 20) {
      return 1;
    } else if (score < 100) {
      return 2;
    } else if (score < 250) {
      return 3;
    } else if (score < 500) {
      return 4;
    } else if (score < 1000) {
      return 5;
    }
  }

  private updateLevel(level: number) {
    this.level = level;
    this.updateSpacesshipByLevel(level);
  }

  private updateSpacesshipByLevel(level: number) {
    switch (level) {
      case 5:
        this.spaceship.fireReloadMax = 1;
        this.spaceship.speed = 8;
        this.spaceship.radius = 48;
        this.spaceship.turnSpeed = Math.PI / 20;
        break;
      case 4:
        this.spaceship.fireReloadMax = 1;
        this.spaceship.speed = 7;
        this.spaceship.radius = 50;
        this.spaceship.turnSpeed = Math.PI / 30;
        break;
      case 3:
        this.spaceship.fireReloadMax = 5;
        this.spaceship.speed = 6;
        this.spaceship.radius = 56;
        this.spaceship.turnSpeed = Math.PI / 40;
        break;
      case 2:
        this.spaceship.fireReloadMax = 10;
        this.spaceship.speed = 5;
        this.spaceship.radius = 60;
        this.spaceship.turnSpeed = Math.PI / 50;
        break;
      case 1:
        this.spaceship.fireReloadMax = 15;
        this.spaceship.speed = 4;
        this.spaceship.radius = 64;
        this.spaceship.turnSpeed = Math.PI / 60;
        break;
    }
    this.initSpacesbodies();
    this.initEnemies();
  }

}
