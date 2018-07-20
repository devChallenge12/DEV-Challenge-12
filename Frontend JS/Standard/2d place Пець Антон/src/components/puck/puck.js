import React from "react";
import PropTypes from "prop-types";
import "./puck.css";

export class Puck extends React.PureComponent {
  constructor(props) {
    super(props);
    this.ref = React.createRef();
  }

  componentDidMount() {
    this.props.getRef(this.ref.current);
  }

  render() {
    return (
      <div
        className={"puck" + (this.props.isPlayer2 ? " puck-player2" : "")}
        ref={this.ref}
      >
        {this.props.isPlayer2 ? "Player 2" : "Player 1"}
      </div>
    );
  }
}

Puck.propTypes = {
  getRef: PropTypes.func.isRequired,
  isPlayer2: PropTypes.bool
};
