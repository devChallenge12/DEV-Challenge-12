import React from "react";
import "./content-container.css";
import { Field } from "Components/field/field";
import { Modal } from "Components/modal/modal";

const MAX_MOVES = 10;

const ContentContainer = class extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      isPlayer2playing: false,
      pointsPlayer1: 0,
      pointsPlayer2: 0,
      moves: 0,
      finished: false
    };

    this.onMoveFinished = this.onMoveFinished.bind(this);
  }

  onMoveFinished(points) {
    if (this.state.isPlayer2playing) {
      this.setState({
        moves: this.state.moves + 1,
        pointsPlayer2: this.state.pointsPlayer2 + points,
        isPlayer2playing: false,
        finished: this.getMovesLeft()-1 === 0 
      })
    } else {
      this.setState({
        moves: this.state.moves + 1,
        pointsPlayer1: this.state.pointsPlayer1 + points,
        isPlayer2playing: true,
        finished: this.getMovesLeft()-1 === 0 
      });
    }
    
  }

  getMovesLeft() {
    return MAX_MOVES - this.state.moves;
  }
  getWinnerMsg() {
    if (this.state.pointsPlayer1 > this.state.pointsPlayer2) {
      return "Player 1 Won!";
    } else if (this.state.pointsPlayer1 > this.state.pointsPlayer2) {
      return "Player 2 Won!";
    } else {
      return "Draw!";
    }
  }

  render() {
    return (
      <div className="content-container">
        <Field
          isPlayer2={this.state.isPlayer2playing}
          onMoveFinish={this.onMoveFinished}
          round={this.state.moves}
        />
        <div className="scoreboard">
          Score
          <b>
            &nbsp;&nbsp;
            {this.state.pointsPlayer1} : {this.state.pointsPlayer2}
          </b>. Moves left: {this.getMovesLeft()}
        </div>
        { !this.state.finished ? null : (
          <Modal
            onClose={() => {
              window.location.reload();
            }}
          >
            {this.getWinnerMsg()}
          </Modal>
        )}
      </div>
    );
  }
};

export { ContentContainer };
