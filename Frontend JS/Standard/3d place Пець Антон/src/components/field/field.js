import { styler, everyFrame, pointer, listen } from "popmotion";
import React from "react";
import "./field.css";
import { Puck } from "Components/puck/puck";
import {
  getTouchData,
  updateCoordianates,
  getYVelocity,
  getXVelocity,
  getThrowDurationFromVelocity,
  getThrowPoints
} from "Services/helpers";

import PropTypes from "prop-types";

const GOAL = {
  x: 320,
  y: 200,
  distance: 100
};

const MAX_X = 270;
const MAX_Y = 400;

export class Field extends React.PureComponent {
  constructor(props) {
    super(props);

    this.isThrowing = false;
    this.touchStart = null;
    this.touchEnd = null;
  }

  componentDidMount() {
    this.play();
  }

  componentDidUpdate() {
    this.play();
  }

  play() {
    const startTracking = e => {
      this.touchStart = getTouchData(e);

      const x = this.puck1Styler.get("x");
      const y = this.puck1Styler.get("y");

      const xPointer = initialX => pointer({ x: initialX }).pipe(({ x }) => x);
      const yPointer = initialY => pointer({ y: initialY }).pipe(({ y }) => y);

      this.pointerXTracker = xPointer(x)
        .filter(pos => Math.abs(pos) <= MAX_X)
        .start(this.puck1Styler.set("x"));

      this.pointerYTracker = yPointer(y)
        .filter(pos => -1 * pos > 0 && -1 * pos < MAX_Y)
        .start(this.puck1Styler.set("y"));
    };

    const stopTracking = e => {
      this.touchEnd = getTouchData(e);
      if (this.pointerXTracker) this.pointerXTracker.stop();
      if (this.pointerYTracker) this.pointerYTracker.stop();

      if (this.touchEnd && this.touchStart) {
        this.throwPuck(this.puck1Styler, this.puck1);
      }
    };

    listen(this.puck1, "mousedown touchstart").start(startTracking);
    listen(this.puck1, "mouseup touchend").start(stopTracking);
  }

  throwPuck(puck, puckDomElement) {
    this.isThrowing = true;

    const xVelocity = getXVelocity(this.touchStart, this.touchEnd);
    const yVelocity = getYVelocity(this.touchStart, this.touchEnd);

    console.log("THROWN");

    const duration = getThrowDurationFromVelocity(xVelocity, yVelocity);

    this.ticker = everyFrame().start(timestamp =>
      updateCoordianates(puck, xVelocity, yVelocity)
    );

    setTimeout(() => {
      this.ticker.stop();
      const points = getThrowPoints(puckDomElement, GOAL);
      console.log(points);
      this.props.onMoveFinish(points);
    }, duration);
  }

  render() {
    return (
      <div className="field">
        <div className="goal"> </div>
        <Puck
          key={this.props.round}
          getRef={puck => {
            this.puck1 = puck;
            this.puck1Styler = styler(puck);
          }}
          isPlayer2={this.props.isPlayer2}
        />
      </div>
    );
  }
}

Field.propTypes = {
  isPlayer2: PropTypes.bool.isRequired,
  onMoveFinish: PropTypes.func.isRequired,
  round: PropTypes.number.isRequired
};
