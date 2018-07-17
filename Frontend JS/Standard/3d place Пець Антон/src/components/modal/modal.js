import React from "react";
import PropTypes from "prop-types";

export class Modal extends React.PureComponent {
  constructor(props) {
    super(props);

    this.keyListener = this.keyListener.bind(this);
  }

  keyListener({ keyCode }) {
    if (keyCode === 27) {
      this.props.onClose();
    }
  }

  componentDidMount() {
    document.addEventListener("keyup", this.keyListener);
  }

  componentWillUnmount() {
    document.removeEventListener("keyup", this.keyListener);
  }

  render() {
    const { children, renderFooter, onClose, renderHeader } = this.props;

    return (
      <div className="modal is-active">
        <div className="modal-background" />
        <div className="modal-card">
          <header className="modal-card-head">
          <p className="modal-card-title">{!!renderHeader && renderHeader()}</p>
            <button
              className="delete"
              aria-label="close"
              onClick={() => onClose()}
            />
          </header>
          <section className="modal-card-body">{children}</section>
          <footer className="modal-card-foot">
            {!!renderFooter && renderFooter()}
          </footer>
        </div>
      </div>
    );
  }
}

Modal.propTypes = {
  onClose: PropTypes.func.isRequired,
  children: PropTypes.node.isRequired,
  renderHeader: PropTypes.func,
  renderFooter: PropTypes.func
};
