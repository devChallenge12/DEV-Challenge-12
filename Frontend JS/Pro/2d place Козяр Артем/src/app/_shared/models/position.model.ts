export interface IPositionModel {
  x: number;
  y: number;
}
export class PositionModel {
  public x: number;
  public y: number;

  constructor(data?: IPositionModel) {
    this.x = data.x || 0;
    this.y = data.y || 0;
  }

}
