import React from "react";
import { Spinner } from "react-bootstrap";

export default function Loader() {
  return (
    <div className="d-flex align-items-center justify-content-center">
      <Spinner animation="border" variant="primary" size="lg" />
    </div>
  );
}
