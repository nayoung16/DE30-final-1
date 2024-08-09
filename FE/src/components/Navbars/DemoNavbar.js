import { logoutUser } from "api/logoutUser";
import "assets/css/argon-design-system-react.css";
import { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import {
  Col,
  Collapse,
  Container,
  Nav,
  Navbar,
  NavbarBrand,
  NavItem,
  NavLink,
  Row,
} from "reactstrap";

const DemoNavbar = ({ isLogin, setIsLogin }) => {
  const [collapseOpen, setCollapseOpen] = useState(false);
  const location = useLocation();

  useEffect(() => {
    setCollapseOpen(false);
  }, [location]);

  const toggleCollapse = () => {
    setCollapseOpen(!collapseOpen);
  };

  const handleLogout = async () => {
    const result = await logoutUser();
    if (result) {
      setIsLogin(false);
    }
  };

  return (
    <header className="header-global">
      <Navbar
        className="navbar-main navbar-transparent navbar-light"
        expand="lg"
        id="navbar-main"
        style={{ height: "100px" }}
      >
        <Container className="d-flex justify-content-between">
          <NavbarBrand className="mr-lg-5" to="/" tag={Link}>
            <img
              alt="..."
              src={require("assets/img/brand/camper1.png")}
              className="img-large"
              style={{ height: "80px" }}
            />
          </NavbarBrand>
          <div className="ml-lg-auto">
            <button
              className="navbar-toggler"
              id="navbar_global"
              onClick={toggleCollapse}
            >
              <span className="navbar-toggler-icon" />
            </button>
            <Collapse isOpen={collapseOpen} navbar>
              <div className="navbar-collapse-header">
                <Row>
                  <Col className="collapse-brand" xs="6">
                    <Link to="/">
                      <img
                        alt="..."
                        src={require("assets/img/brand/camper1.png")}
                      />
                    </Link>
                  </Col>
                  <Col className="collapse-close" xs="6">
                    <button
                      className="navbar-toggler"
                      id="navbar_global"
                      onClick={toggleCollapse}
                    >
                      <span />
                      <span />
                    </button>
                  </Col>
                </Row>
              </div>
              <Nav className="navbar-nav-hover align-items-lg-center" navbar>
                <NavItem>
                  <NavLink to="/search-page" tag={Link}>
                    Search
                  </NavLink>
                </NavItem>
                <NavItem>
                  <NavLink to="/profile-page" tag={Link}>
                    Profile
                  </NavLink>
                </NavItem>
                <NavItem>
                  <NavLink to="/recommand-page" tag={Link}>
                    Recommend
                  </NavLink>
                </NavItem>
                {!isLogin && (
                  <NavItem>
                    <NavLink to="/register-page" tag={Link}>
                      Register
                    </NavLink>
                  </NavItem>
                )}
                <NavItem>
                  {isLogin ? (
                    <NavLink
                      to="/"
                      tag={Link}
                      onClick={handleLogout}
                      style={{
                        textTransform: "none",
                        color: "white",
                        fontWeight: "normal",
                      }}
                    >
                      Logout
                    </NavLink>
                  ) : (
                    <NavLink to="/login-page" tag={Link}>
                      Login
                    </NavLink>
                  )}
                </NavItem>
              </Nav>
            </Collapse>
          </div>
        </Container>
      </Navbar>
    </header>
  );
};

export default DemoNavbar;
