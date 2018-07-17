import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  ViewEncapsulation,
  ElementRef,
  AfterViewInit
} from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';
import { MatDialog,
        MatDialogRef,
        MatSnackBar,
        MatButtonModule,
        MatIcon } from '@angular/material';
import { Subscription, Subject } from 'rxjs';
import { Cookie } from 'ng2-cookies/ng2-cookies';
import 'jquery';
import 'jquery-ui-dist/jquery-ui';
import * as $ from 'jquery';
import * as THREE from 'three';

@Component({
  selector: 'app',
  encapsulation: ViewEncapsulation.None,
  styleUrls: [
    './app.component.scss',
    './../../node_modules/@angular/material/prebuilt-themes/indigo-pink.css'
  ],
  templateUrl: 'app.component.html',
  providers: [AuthenticationService]
})
export class AppComponent implements OnInit {
  @ViewChild('rendererContainer') public rendererContainer: ElementRef;
  public renderer = new THREE.WebGLRenderer();
  public scene = null;
  public camera = null;
  public mesh = null;
  public fireArray = [];
  public enemyArray = [];
  public bonusArray = [];
  public materials = null;
  public materials30 = null;
  public materials50 = null;
  public materials80 = null;
  public materials100 = null;
  public materials150 = null;
  public materialsEnemy = null;
  public points: number = 0;
  public addMissileFlag: boolean = false;


  constructor() {

  }

  public missileFactory() {

    if (this.points < 300) {
      let geometry = new THREE.BoxGeometry(10, 10, 10);
      let fireItem = new THREE.Mesh(geometry, this.materials);
      this.fireArray.push(fireItem);
      fireItem.position.y = -500;
      fireItem.position.x = this.mesh.position.x;
      this.scene.add(fireItem);
    } else if (this.points < 500) {
      let geometry = new THREE.BoxGeometry(10, 10, 10);
      let fireItem = new THREE.Mesh(geometry, this.materials);
      this.fireArray.push(fireItem);
      fireItem.position.y = -500;
      fireItem.position.x = this.mesh.position.x + 15;
      this.scene.add(fireItem);

      let geometry2 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem2 = new THREE.Mesh(geometry2, this.materials);
      this.fireArray.push(fireItem2);
      fireItem2.position.y = -500;
      fireItem2.position.x = this.mesh.position.x - 15;
      this.scene.add(fireItem2);
    } else if (this.points < 800) {
      let geometry = new THREE.BoxGeometry(10, 10, 10);
      let fireItem = new THREE.Mesh(geometry, this.materials);
      this.fireArray.push(fireItem);
      fireItem.position.y = -500;
      fireItem.position.x = this.mesh.position.x - 20;
      this.scene.add(fireItem);

      let geometry2 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem2 = new THREE.Mesh(geometry2, this.materials);
      this.fireArray.push(fireItem2);
      fireItem2.position.y = -500;
      fireItem2.position.x = this.mesh.position.x;
      this.scene.add(fireItem2);

      let geometry3 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem3 = new THREE.Mesh(geometry3, this.materials);
      this.fireArray.push(fireItem3);
      fireItem3.position.y = -500;
      fireItem3.position.x = this.mesh.position.x + 20;
      this.scene.add(fireItem3);

    } else if (this.points < 1000) {
      let geometry = new THREE.BoxGeometry(10, 10, 10);
      let fireItem = new THREE.Mesh(geometry, this.materials);
      this.fireArray.push(fireItem);
      fireItem.position.y = -500;
      fireItem.position.x = this.mesh.position.x - 30;
      this.scene.add(fireItem);

      let geometry2 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem2 = new THREE.Mesh(geometry2, this.materials);
      this.fireArray.push(fireItem2);
      fireItem2.position.y = -500;
      fireItem2.position.x = this.mesh.position.x - 15;
      this.scene.add(fireItem2);

      let geometry3 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem3 = new THREE.Mesh(geometry3, this.materials);
      this.fireArray.push(fireItem3);
      fireItem3.position.y = -500;
      fireItem3.position.x = this.mesh.position.x + 15;
      this.scene.add(fireItem3);

      let geometry4 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem4 = new THREE.Mesh(geometry4, this.materials);
      this.fireArray.push(fireItem4);
      fireItem4.position.y = -500;
      fireItem4.position.x = this.mesh.position.x + 30;
      this.scene.add(fireItem4);
    } else {
      let geometry = new THREE.BoxGeometry(10, 10, 10);
      let fireItem = new THREE.Mesh(geometry, this.materials);
      this.fireArray.push(fireItem);
      fireItem.position.y = -500;
      fireItem.position.x = this.mesh.position.x - 35;
      this.scene.add(fireItem);

      let geometry2 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem2 = new THREE.Mesh(geometry2, this.materials);
      this.fireArray.push(fireItem2);
      fireItem2.position.y = -500;
      fireItem2.position.x = this.mesh.position.x - 20;
      this.scene.add(fireItem2);

      let geometry3 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem3 = new THREE.Mesh(geometry3, this.materials);
      this.fireArray.push(fireItem3);
      fireItem3.position.y = -500;
      fireItem3.position.x = this.mesh.position.x;
      this.scene.add(fireItem3);

      let geometry4 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem4 = new THREE.Mesh(geometry4, this.materials);
      this.fireArray.push(fireItem4);
      fireItem4.position.y = -500;
      fireItem4.position.x = this.mesh.position.x + 20;
      this.scene.add(fireItem4);

      let geometry5 = new THREE.BoxGeometry(10, 10, 10);
      let fireItem5 = new THREE.Mesh(geometry5, this.materials);
      this.fireArray.push(fireItem5);
      fireItem5.position.y = -500;
      fireItem5.position.x = this.mesh.position.x + 35;
      this.scene.add(fireItem5);
    }
    console.log('memory control', 'missile: ' + this.fireArray.length, 'enemy: ' + this.enemyArray.length);
  }

  public enemyFactory() {
    let type: number = 30;
    let randomType = 2 * Math.round(Math.random() * 100);
    if (randomType <= 30) {
      type = 30;
    } else if (randomType <= 50) {
      type = 50;
    } else if (randomType <= 80) {
      type = 80;
    } else if (randomType <= 100) {
      type = 100;
    } else {
      type = 150;
    }
    let geometry = new THREE.BoxGeometry(type, type, type);
    let enemyItem = new THREE.Mesh(geometry, this.materialsEnemy);
    // this.enemyArray.push(enemyItem);
    this.enemyArray.push({type, enemyItem});
    enemyItem.position.y = 600;
    // enemyItem.position.x = this.mesh.position.x;
    enemyItem.position.x = -1100 + Math.round(Math.random() * 2200);
    this.scene.add(enemyItem);
  }

  public bonusFactory() {
    let geometry = new THREE.BoxGeometry(50, 50, 50);
    let type: number = 30;
    let bonusItem;
    let randomType = 2 * Math.round(Math.random() * 100);
    if (randomType <= 30) {
      type = 30;
      bonusItem = new THREE.Mesh(geometry, this.materials30);
    } else if (randomType <= 50) {
      type = 50;
      bonusItem = new THREE.Mesh(geometry, this.materials50);
    } else if (randomType <= 80) {
      type = 80;
      bonusItem = new THREE.Mesh(geometry, this.materials80);
    } else if (randomType <= 100) {
      type = 100;
      bonusItem = new THREE.Mesh(geometry, this.materials100);
    } else {
      type = 150;
      bonusItem = new THREE.Mesh(geometry, this.materials150);
    }
    this.bonusArray.push({type, bonusItem});
    bonusItem.position.y = -500;
    bonusItem.position.y = 600;
    // enemyItem.position.x = this.mesh.position.x;
    bonusItem.position.x = -1100 + Math.round(Math.random() * 2200);
    this.scene.add(bonusItem);
  }

  public checkMissileAndEnemyCollision() {
    for (let i = 0; i < this.fireArray.length; i++) {
      let collisionFlag: boolean = false;

      for (let k = 0; k < this.enemyArray.length; k++) { // O (N**2) :-)
        let missile = this.fireArray[i];
        let enemy = this.enemyArray[k]['enemyItem'];
        if ((missile.position.x <= enemy.position.x + this.enemyArray[k]['type'])
        && (missile.position.x >= enemy.position.x - this.enemyArray[k]['type'])
        && (missile.position.y <= enemy.position.y + this.enemyArray[k]['type'])
        && (missile.position. y >= enemy.position.y - this.enemyArray[k]['type'])) {
          console.log('hitting');
          this.points += 10;
          $('#pointsBlock').text('Points: ' + this.points);
          collisionFlag = true;
          this.scene.remove(this.enemyArray[k]['enemyItem']);
          this.enemyArray.splice(k, 1);
          k--;
        }
      }

      if (collisionFlag) {
        this.scene.remove(this.fireArray[i]);
        this.fireArray.splice(i, 1);
        i--;
      }
    }
  }

  public checkMeAndEnemyCollision() {
    let collisionFlag: boolean = false;

    for (let k = 0; k < this.enemyArray.length; k++) { // O (N**2) :-)
      let me = this.mesh;
      let enemy = this.enemyArray[k]['enemyItem'];
      if ((me.position.x <= enemy.position.x + 100)
      && (me.position.x >= enemy.position.x - 100)
      && (me.position.y <= enemy.position.y + 100)
      && (me.position. y >= enemy.position.y - 100)) {
        console.log('hitting');
        this.points = 0;
        $('#pointsBlock').text('Points: ' + this.points + ' (From start - collision with enemy)');
        collisionFlag = true;
        this.scene.remove(this.enemyArray[k]['enemyItem']);
        this.enemyArray.splice(k, 1);
        k--;
      }
    }
  }

  public checkMeAndBonusCollision() {
    let collisionFlag: boolean = false;

    for (let k = 0; k < this.bonusArray.length; k++) { // O (N**2) :-)
      let me = this.mesh;
      let bonus = this.bonusArray[k]['bonusItem'];
      if ((me.position.x <= bonus.position.x + 100)
      && (me.position.x >= bonus.position.x - 100)
      && (me.position.y <= bonus.position.y + 100)
      && (me.position. y >= bonus.position.y - 100)) {
        console.log('catch');

        this.points += this.bonusArray[k]['type'];
        $('#pointsBlock').text('Points: ' + this.points);
        // collisionFlag = true;
        this.scene.remove(this.bonusArray[k]['bonusItem']);
        this.bonusArray.splice(k, 1);
        k--;
      }
    }
  }

  public ngOnInit() {

    this.scene = new THREE.Scene();
    $('#mainEl').focus();
    $('#pointsBlock').text('Points: ' + this.points);

    this.camera = new THREE.PerspectiveCamera(75, 600 / 400, 1, 10000);
    // this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 1, 10000);
    this.camera.position.z = 1000;

    const light = new THREE.DirectionalLight( 0xffffff );
    light.position.set( 0, 1, 1 ).normalize();
    this.scene.add(light);

    const ambient = new THREE.AmbientLight ( 0x555555 );
    this.scene.add(ambient);

    this.createRubixMaterial();

    const geometry = new THREE.BoxGeometry(100, 100, 100);
    this.mesh = new THREE.Mesh(geometry, this.materials);
    this.mesh.position.y = -600;

    this.scene.add(this.mesh);

    $(window).on('resize', () => {
      this.renderer.setSize(window.innerWidth - 5, window.innerHeight - 25);
    });

    $(document).on('keydown', (e: any) => {
        if (e.keyCode === 37) { // left
          if (this.mesh.position.x >= -1100) {
            this.mesh.position.x -= 30; // left limit
          }
        }
        if (e.keyCode === 39) { // right
          if (this.mesh.position.x <= 1100) {
            this.mesh.position.x += 30;
          }
        }

        if (e.keyCode === 32) { // fire
          this.addMissileFlag = true;
        }
    });

  }

  createRubixMaterial() {
    const material1 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics1.jpg') } );
    const material2 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics2.jpg') } );
    const material3 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics3.jpg') } );
    const material4 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics4.jpg') } );
    const material5 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics5.jpg') } );
    const material6 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics6.jpg') } );

    this.materials = [material1, material2, material3, material4, material5, material6];

    // material for bonuses

    const material30 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics30.jpg') } );
    this.materials30 = [material30, material30, material30, material30, material30, material30];

    const material50 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics50.jpg') } );
    this.materials50 = [material50, material50, material50, material50, material50, material50];

    const material80 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics80.jpg') } );
    this.materials80 = [material80, material80, material80, material80, material80, material80];

    const material100 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics100.jpg') } );
    this.materials100 = [material100, material100, material100, material100, material100, material100];

    const material150 = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubics150.jpg') } );
    this.materials150 = [material150, material150, material150, material150, material150, material150];

    // for enemies
    const materialEnemy = new THREE.MeshPhongMaterial( { map: new THREE.TextureLoader().load('../assets/rubicsEnemy.jpg') } );
    this.materialsEnemy = [materialEnemy, materialEnemy, materialEnemy, materialEnemy, materialEnemy, materialEnemy];
  }

  ngAfterViewInit() {
      this.renderer.setSize(window.innerWidth - 5, window.innerHeight - 25);
      this.renderer.domElement.style.display = 'block';
      this.renderer.domElement.style.margin = 'auto';
      this.rendererContainer.nativeElement.appendChild(this.renderer.domElement);
      setInterval(()=> {
        this.enemyFactory();
        this.bonusFactory();
      }, 3000);

      this.animate();
  }

  animate() {
    window.requestAnimationFrame(() => this.animate());
      this.mesh.rotation.y += 0.02;
      this.renderer.render(this.scene, this.camera);
      for (let i = 0; i < this.fireArray.length; i++) {
        this.fireArray[i].rotation.y += 0.02;
        this.fireArray[i].position.y += 10;
        if (this.fireArray[i].position.y > 600) {
          this.scene.remove(this.fireArray[i]);
          this.fireArray.splice(i, 1);
          i--;
        }
      }

      for (let i = 0; i < this.enemyArray.length; i++) {
        this.enemyArray[i]['enemyItem'].rotation.y += 0.02;
        this.enemyArray[i]['enemyItem'].position.y -= 5;
        if (this.enemyArray[i]['enemyItem'].position.y < -700) {
          this.scene.remove(this.enemyArray[i]['enemyItem']);
          this.enemyArray.splice(i, 1);
          i--;
        }
      }

      for (let i = 0; i < this.bonusArray.length; i++) {
        this.bonusArray[i]['bonusItem'].rotation.y += 0.02;
        this.bonusArray[i]['bonusItem'].position.y -= 5;
        if (this.bonusArray[i]['bonusItem'].position.y < -700) {
          this.scene.remove(this.bonusArray[i]['bonusItem']);
          this.bonusArray.splice(i, 1);
          i--;
        }
      }

      this.checkMissileAndEnemyCollision();
      this.checkMeAndEnemyCollision();
      this.checkMeAndBonusCollision();

      if (this.addMissileFlag) {
        this.missileFactory();
        this.addMissileFlag = false;
      }
  }
}
