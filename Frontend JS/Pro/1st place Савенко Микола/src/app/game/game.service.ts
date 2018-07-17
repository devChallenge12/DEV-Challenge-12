import * as THREE from 'three';

(<any>window).THREE = THREE;

const TEXTURES = {
  BG: '/fw/assets/images/bg.jpg',
  SHIP: [
    '/fw/assets/images/ship-1.png',
    '/fw/assets/images/ship-2.png',
    '/fw/assets/images/ship-3.png',
    '/fw/assets/images/ship-4.png',
    '/fw/assets/images/ship-5.png'
  ],
  EL: [
    '/fw/assets/images/el-1.png',
    '/fw/assets/images/el-2.png',
    '/fw/assets/images/el-3.png',
    '/fw/assets/images/el-4.png',
    '/fw/assets/images/el-5.png'
  ],
  EN: [
    '/fw/assets/images/en-1.png',
    '/fw/assets/images/en-2.png',
    '/fw/assets/images/en-3.png',
    '/fw/assets/images/en-4.png',
    '/fw/assets/images/en-5.png'
  ]
};

export class GameRendererService {

  public renderer: any;
  private container: Element;
  private scene: THREE.Scene;
  private camera: THREE.PerspectiveCamera;

  private animateId: any;
  private elementsTimer: any;

  ship;
  fires: any[] = [];
  elements: any[] = [];
  enemies: any[] = [];
  score = 0;
  level = 1;

  constructor(container) {

    this.container = container;

    this.renderer = new THREE.WebGLRenderer({antialias: false});
    this.renderer.setSize(window.innerWidth, window.innerHeight);
    this.renderer.setPixelRatio(window.devicePixelRatio);
    this.renderer.setClearColor(0x000000, 0);
    this.onKeyDown = this.onKeyDown.bind(this);
  }

  /**
   * Initialize
   */
  init() {
    const loader = new THREE.TextureLoader();
    loader.load(TEXTURES.BG, (texture) => {
      this.initScene(texture);
      this.startAnimation();
      this.addEventListeners();
      this.addElements();
      this.addEnemies();
    });
  }

  /**
   * Initialize camera
   * @private
   */
  private initCamera() {
    this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 100);
    this.camera.position.z = 100;
  }

  /**
   * Add scene background image, build spaceship and start rendering
   * @param texture
   */
  initScene(texture) {
    this.initCamera();

    if (!this.scene) {
      this.scene = new THREE.Scene();
    }

    texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
    texture.offset.set(0, 0);
    texture.repeat.set(1, 1);

    const backgroundMesh = new THREE.Mesh(
      new THREE.PlaneGeometry(window.innerWidth / 4, window.innerHeight / 4, 0),
      new THREE.MeshBasicMaterial({
        map: texture
      }));

    this.scene.add(backgroundMesh);

    this.buildShip();

    if (!this.isCanvasElExisted()) {
      this.renderer.render(this.scene, this.camera);
      this.renderer.setSize(window.innerWidth, window.innerHeight);
      this.container.appendChild(this.renderer.domElement);
    }
  }

  /**
   * Build a new spaceship depends on the current level
   */
  private buildShip() {

    const loader = new THREE.TextureLoader();
    loader.load(TEXTURES.SHIP[this.level - 1], (texture) => {

      const geometry = new THREE.PlaneGeometry(20, 15, 0);
      const material = new THREE.MeshBasicMaterial({
        map: texture,
        transparent: true,
        side: THREE.DoubleSide
      });

      this.ship = new THREE.Mesh(geometry, material);
      this.ship.name = 'ship';

      this.ship.geometry.applyMatrix(new THREE.Matrix4().makeTranslation(-120, 0, 0));
      this.scene.add(this.ship);
    });
  }

  /**
   * Start animate and update camera position
   */
  startAnimation() {
    this.cancelAnimation();

    this.animateId = requestAnimationFrame(() => {
      this.renderer.render(this.scene, this.camera);

      this.firesAnimate();
      this.elementsAnimate();
      this.enemiesAnimate();

      this.checkObjectCollisions();

      this.startAnimation();
    });
  }

  /**
   * Cancel animation by animateId
   */
  cancelAnimation() {
    if (this.animateId) {
      cancelAnimationFrame(this.animateId);
      this.animateId = undefined;
    }
  }

  private isCanvasElExisted(): boolean {
    return !!this.container.querySelector('canvas');
  }


  private addEventListeners() {
    document.addEventListener('keydown', this.onKeyDown, false);
  }

  /**
   * Handler fires on press key top or left
   * @param e
   */
  onKeyDown(e) {
    const keyCode = e.which;

    if (keyCode === 38) {
      this.ship.position.y += 5;
    } else if (keyCode === 40) {
      this.ship.position.y -= 5;
    }

    if (keyCode === 32) {
      this.onFire();
    }
  }

  /**
   * Add/Generate new bullets depends on the level
   */
  private onFire() {
    const geometry = new THREE.PlaneGeometry(1, 1, 0);
    const material = new THREE.MeshBasicMaterial({color: 0xff0000, side: THREE.DoubleSide});
    const fire = new THREE.Mesh(geometry, material);

    fire.position.y = this.ship.position.y;
    fire.position.x = -120;
    fire.name = 'fire';

    const fires = [];

    for (let i = 0; i < this.level; i++) {
      const f = fire.clone();
      f.position.y = this.ship.position.y - 3 * i;
      fires.push(f);
      this.scene.add(f);
    }

    this.fires.push(...fires);
  }

  /**
   * Add/Generate new elements depends on the level
   */
  addElements() {
    const loader = new THREE.TextureLoader();

    loader.load(TEXTURES.EL[this.level - 1], (texture) => {
      const geometry = new THREE.PlaneGeometry(8, 8, 0);
      const material = new THREE.MeshBasicMaterial({
        map: texture,
        transparent: true,
        side: THREE.DoubleSide
      });
      const element = new THREE.Mesh(geometry, material);

      const n = Math.random();
      element.position.y = n * (n < 0.5 ? -1 : 1) * 50;
      element.position.x = 120;
      element.name = 'element';

      this.elementsTimer = setTimeout(() => {
        this.addElements();
        this.elements.push(element);
        this.scene.add(element);
      }, 1000);

    });
  }

  /**
   * Add/Generate new enemies depends on the level
   */
  addEnemies() {
    const loader = new THREE.TextureLoader();

    loader.load(TEXTURES.EN[this.level - 1], (texture) => {

      const geometry = new THREE.PlaneGeometry(20, 20, 0);
      const material = new THREE.MeshBasicMaterial({
        map: texture,
        transparent: true,
        side: THREE.DoubleSide
      });
      const enemy = new THREE.Mesh(geometry, material);

      const n = Math.random();
      enemy.position.y = n * (n < 0.5 ? -1 : 1) * 50;
      enemy.position.x = 120;
      enemy.name = 'enemy';

      this.elementsTimer = setTimeout(() => {
        this.addEnemies();
        this.enemies.push(enemy);
        this.scene.add(enemy);
      }, 2000);
    });
  }

  /**
   * Change position of enemies
   */
  enemiesAnimate() {
    this.enemies.forEach((v) => {
      v.position.x -= 1;
      v.position.y -= Math.random() < 0.5 ? -1 : 1;
    });
  }

  /**
   * Change position of elements
   */
  elementsAnimate() {
    this.elements.forEach((v) => {
      v.position.x -= 2;
    });
  }

  /**
   * Change position of fires
   */
  firesAnimate() {
    this.fires.forEach((v) => {
      v.position.x += 2;
    });
  }

  checkObjectCollisions() {

    this.scene.traverse((node) => {

      if (['element'].indexOf(node.name) > -1) {

        if (this.ship.position.x === node.position.x + 120 &&
          this.ship.position.y + 5 > node.position.y && this.ship.position.y - 10 < node.position.y) {
          this.score++;
        }
      }
    });

    this.changeLevel();
  }

  /**
   * Change level and upgrade ship
   */
  changeLevel() {
    if (this.score > 4 && this.score < 10) {
      this.level = 2;
      this.upgradeShip();
    }

    if (this.score > 9 && this.score < 14) {
      this.level = 3;
      this.upgradeShip();
    }

    if (this.score > 14 && this.score < 19) {
      this.level = 4;
      this.upgradeShip();
    }

    if (this.score > 19) {
      this.level = 5;
      this.upgradeShip();
    }
  }

  /**
   * Upgrade ship skin
   */
  upgradeShip() {
    const loader = new THREE.TextureLoader();
    loader.load(TEXTURES.SHIP[this.level - 1], (texture) => {

      this.ship.material = new THREE.MeshBasicMaterial({
        map: texture,
        transparent: true,
        side: THREE.DoubleSide
      });
    });
  }

}
