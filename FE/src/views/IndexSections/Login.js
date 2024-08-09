import SimpleFooter from "components/Footers/SimpleFooter.js";
import DemoNavbar from "components/Navbars/DemoNavbar.js";
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Col,
  Container,
  Form,
  FormGroup,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Row,
} from "reactstrap";
import { loginUser } from '../../api/loginUser';
import { logoutUser } from '../../api/logoutUser'; // 로그아웃 함수 추가
import { postLoginToken } from '../../api/postLoginToken';
import GoogleLogin from '../../components/Login/GoogleLogin';

export default function Login({ isLogin, setIsLogin }) {
  const navigate = useNavigate();
  const mainRef = useRef(null);
  const [form, setForm] = useState({ id: '', pw: '' });

  useEffect(() => {
    document.documentElement.scrollTop = 0;
    document.scrollingElement.scrollTop = 0;
    if (mainRef.current) {
      mainRef.current.scrollTop = 0;
    }
  }, []);

  useEffect(() => {
    if (isLogin) {
      navigate('/Profile');
    }
  }, [isLogin, navigate]);

  const onGoogleSignIn = async (res) => {
    const { credential } = res;
    console.log('Google Credential:', credential); // 로그 추가
    const result = await postLoginToken({ idToken: credential }, setIsLogin);
    setIsLogin(result);
  };

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    const result = await loginUser(form);
    if (result) {
      setIsLogin(true);
      navigate('/Profile');
    } else {
      alert('Login failed. Please check your credentials.');
    }
  };

  const handleLogout = async () => {
    const result = await logoutUser();
    if (result) {
      setIsLogin(false);
      navigate('/');
    } else {
      alert('Logout failed.');
    }
  };

  return (
    <>
      <DemoNavbar />
      <main ref={mainRef}>
        <section className="section section-shaped section-lg">
          <div className="shape shape-style-1 bg-gradient-default">
            <span />
            <span />
            <span />
            <span />
            <span />
            <span />
            <span />
            <span />
          </div>
          <Container className="pt-lg-7">
            <Row className="justify-content-center">
              <Col lg="5">
                <Card className="bg-secondary shadow border-0">
                  <CardHeader className="bg-white pb-5">
                    <div className="text-muted text-center mb-3">
                      <small>Sign in with</small>
                    </div>
                    <div className="btn-wrapper text-center">
                      <Button
                        className="btn-neutral btn-icon"
                        color="default"
                        href="#pablo"
                        onClick={(e) => e.preventDefault()}
                      >
                        <span className="btn-inner--icon mr-1">
                          <img
                            alt="..."
                            src={require('assets/img/icons/common/github.svg').default}
                          />
                        </span>
                        <span className="btn-inner--text">Github</span>
                      </Button>
                      <GoogleLogin
                        onGoogleSignIn={onGoogleSignIn}
                        render={(renderProps) => (
                          <Button
                            className="btn-neutral btn-icon ml-1"
                            color="default"
                            href="#pablo"
                            onClick={(e) => {
                              e.preventDefault();
                              renderProps.onClick();
                            }}
                          >
                            <span className="btn-inner--icon mr-1">
                              <img
                                alt="..."
                                src={require('assets/img/icons/common/google.svg').default}
                              />
                            </span>
                            <span className="btn-inner--text">Google</span>
                          </Button>
                        )}
                        onFailure={(res) => console.log('로그인 실패:', res)}
                        cookiePolicy={'single_host_origin'}
                      />
                    </div>
                  </CardHeader>
                  <CardBody className="px-lg-5 py-lg-5">
                    <div className="text-center text-muted mb-4">
                      <small>Or sign in with credentials</small>
                    </div>
                    <Form role="form" onSubmit={handleSubmit}>
                      <FormGroup className="mb-3">
                        <InputGroup className="input-group-alternative">
                          <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                              <i className="ni ni-email-83" />
                            </InputGroupText>
                          </InputGroupAddon>
                          <Input 
                            placeholder="Email" 
                            type="email" 
                            name="id" 
                            value={form.id} 
                            onChange={handleChange} 
                          />
                        </InputGroup>
                      </FormGroup>
                      <FormGroup>
                        <InputGroup className="input-group-alternative">
                          <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                              <i className="ni ni-lock-circle-open" />
                            </InputGroupText>
                          </InputGroupAddon>
                          <Input
                            placeholder="Password"
                            type="password"
                            name="pw" 
                            value={form.pw} 
                            onChange={handleChange} 
                            autoComplete="off"
                          />
                        </InputGroup>
                      </FormGroup>
                      <div className="custom-control custom-control-alternative custom-checkbox">
                        <input
                          className="custom-control-input"
                          id="customCheckLogin"
                          type="checkbox"
                        />
                        <label
                          className="custom-control-label"
                          htmlFor="customCheckLogin"
                        >
                          <span>Remember me</span>
                        </label>
                      </div>
                      <div className="text-center">
                        <Button className="my-4" color="primary" type="submit">
                          Sign in
                        </Button>
                      </div>
                    </Form>
                    {isLogin && (
                      <div className="text-center">
                        <Button className="my-4" color="danger" onClick={handleLogout}>
                          Logout
                        </Button>
                      </div>
                    )}
                  </CardBody>
                </Card>
                <Row className="mt-3">
                  <Col xs="6">
                    <a
                      className="text-light"
                      href="#pablo"
                      onClick={(e) => e.preventDefault()}
                    >
                      <small>Forgot password?</small>
                    </a>
                  </Col>
                  <Col className="text-right" xs="6">
                    <a
                      className="text-light"
                      href="#pablo"
                      onClick={(e) => e.preventDefault()}
                    >
                      <small>Create new account</small>
                    </a>
                  </Col>
                </Row>
              </Col>
            </Row>
          </Container>
        </section>
      </main>
      <SimpleFooter />
    </>
  );
}