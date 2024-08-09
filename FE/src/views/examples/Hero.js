// components/Hero/Hero.js
import "assets/css/argon-design-system-react.css";
import "assets/css/argon-design-system-react.min.css";
import React from "react";
import { Link } from "react-router-dom";
import { Button, Col, Container, Row } from "reactstrap";

export default function Hero ({ isLogin, setIsLogin }) {

    return (
      <>
        <div className="position-relative">
          <section className="section section-hero section-shaped">
            <div className="shape shape-style-1 shape-custom5"></div>
            <Container className="shape-container d-flex align-items-center py-lg">
              <div className="col px-0">
                <Row className="align-items-center justify-content-center">
                  <Col className="text-center" lg="6">
                    <img
                      alt="..."
                      className="img-fluid"
                      src={require("assets/img/brand/camper.png")}
                      style={{ width: "300px" }}
                    />
                    <p class="fontNeo" style={{color:"white", marginTop:"10px", fontSize:"20px"}}>당신만의 캠핑 스타일을 찾아보세요</p>
                    <div className="btn-wrapper mt-5" style={{ display: "flex", justifyContent: "center" }}>
                    {isLogin ? (
                        <>
                          <Link to="/profile-page">
                            <Button
                              className="btn-white btn-icon mb-3 mb-sm-0"
                              color="white"
                              size="lg"
                              style={{ width: "150px", margin: "0 10px" }}
                            >
                              <span className="btn-inner--icon mr-1">
                                <i className="ni ni-circle-08" />
                              </span>
                              <span className="btn-inner--text">Profile</span>
                            </Button>
                          </Link>
                          <Link to="/recommand-page">
                            <Button
                              className="btn-icon mb-3 mb-sm-0"
                              color="white"
                              size="lg"
                              style={{ width: "180px", margin: "0 10px" }}
                            >
                              <span className="btn-inner--icon mr-1">
                              <i className="ni ni-single-copy-04" />
                              </span>
                              <span className="btn-inner--text">Recommend</span>
                            </Button>
                          </Link>
                        </>
                      ) : (
                        <>
                          <Link to="/register-page">
                            <Button
                              className="btn-white btn-icon mb-3 mb-sm-0"
                              color="white"
                              size="lg"
                              style={{ width: "150px", margin: "0 10px" }}
                            >
                              <span className="btn-inner--icon mr-1">
                                <i className="ni ni-single-copy-04" />
                              </span>
                              <span className="btn-inner--text">Register</span>
                            </Button>
                          </Link>
                          <Link to="/login-page">
                            <Button
                              className="btn-icon mb-3 mb-sm-0"
                              color="white"
                              size="lg"
                              style={{ width: "150px", margin: "0 10px" }}
                            >
                              <span className="btn-inner--icon mr-1">
                                <i className="ni ni-circle-08" />
                              </span>
                              <span className="btn-inner--text">Login</span>
                            </Button>
                          </Link>
                        </>
                      )}
                    </div>
                      {/* <Link to="/register-page">
                        <Button
                          className="btn-white btn-icon mb-3 mb-sm-0"
                          color="white"
                          size="lg"
                          style={{ width: "150px", margin: "0 10px" }}
                        >
                          <span className="btn-inner--icon mr-1">
                            <i className="ni ni-single-copy-04" />
                          </span>
                          <span className="btn-inner--text">Register</span>
                        </Button>
                      </Link>
                      <Link to="/login-page">
                        <Button
                          className="btn-icon mb-3 mb-sm-0"
                          color="white"
                          size="lg"
                          style={{ width: "150px", margin: "0 10px" }}
                        >
                          <span className="btn-inner--icon mr-1">
                            <i className="ni ni-circle-08" />
                          </span>
                          <span className="btn-inner--text">Login</span>
                        </Button>
                      </Link>
                    </div> */}
                    <div className="mt-5">
                      <small class="fontNL" style={{color:"white", fontSize:"15px"}}>
                        맛동산학회
                      </small>
                    </div>
                    <div></div>
                  </Col>
                </Row>
              </div>
            </Container>
            <div className="separator separator-bottom separator-skew zindex-100">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                preserveAspectRatio="none"
                version="1.1"
                viewBox="0 0 2560 100"
                x="0"
                y="0"
              >
                <polygon
                  className="fill-white"
                  points="2560 0 2560 100 0 100"
                />
              </svg>
            </div>
          </section>
        </div>
      </>
    );
}
