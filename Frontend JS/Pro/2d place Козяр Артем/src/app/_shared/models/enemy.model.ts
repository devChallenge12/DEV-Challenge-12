import {SpacesbodyModel} from './spacesbody.model';

export class EnemyModel extends SpacesbodyModel {

  constructor(data?: any) {
    super(data);
    this.radius = 32;
    this.speed = 2;
    this.color = 'red';
  }

}
