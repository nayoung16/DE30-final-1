import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Button,
  Card,
  CardBody,
  Col,
  Container,
  Form,
  FormGroup,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Row
} from "reactstrap";
import { registerUser } from "../../api/registerUser"; // registerUser API 함수 추가
import Modal from "react-modal";

import DemoNavbar from "components/Navbars/DemoNavbar.js";

const Register = () => {
  const mainRef = useRef(null);
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState("");

  useEffect(() => {
    document.documentElement.scrollTop = 0;
    document.scrollingElement.scrollTop = 0;
    if (mainRef.current) {
      mainRef.current.scrollTop = 0;
    }
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await registerUser(form);
    if (result) {
      navigate("/login");
    } else {
      setModalMessage("Registration failed. 유저가 이미 존재하거나 다른 오류임");
      setModalIsOpen(true);
    }
  };

  return (
    <>
      <DemoNavbar />
      <main ref={mainRef}>
        <section className="section section-shaped section-lg">
          <div className="shape shape-style-1 shape-custom">
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
                  {/* <CardHeader className="bg-white pb-5">
                    <div className="text-muted text-center mb-3">
                      <small>Sign up with</small>
                    </div>
                    <div className="text-center">
                       <Button
                        className="btn-neutral btn-icon mr-4"
                        color="default"
                        href="#pablo"
                        onClick={(e) => e.preventDefault()}
                      >
                        <span className="btn-inner--icon mr-1">
                          <img
                            alt="..."
                            src={
                              require("assets/img/icons/common/github.svg")
                                .default
                            }
                          />
                        </span>
                        <span className="btn-inner--text">Github</span>
                      </Button>
                      <Button
                        className="btn-neutral btn-icon ml-1"
                        color="default"
                        href="#pablo"
                        onClick={(e) => e.preventDefault()}
                      >
                        <span className="btn-inner--icon mr-1">
                          <img
                            alt="..."
                            src={
                              require("assets/img/icons/common/google.svg")
                                .default
                            }
                          />
                        </span>
                        <span className="btn-inner--text">Google</span>
                      </Button>
                    </div>
                  </CardHeader> */}
                  <CardBody className="px-lg-5 py-lg-5">
                    <div className="text-center text-muted mb-4">
                      <strong>Sign up with credentials</strong>
                    </div>
                    <Form role="form" onSubmit={handleSubmit}>
                      <FormGroup>
                        <InputGroup className="input-group-alternative mb-3">
                          <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                              <i className="ni ni-hat-3" />
                            </InputGroupText>
                          </InputGroupAddon>
                          <Input
                            placeholder="Name"
                            type="text"
                            name="name"
                            value={form.name}
                            onChange={handleChange}
                          />
                        </InputGroup>
                      </FormGroup>
                      <FormGroup>
                        <InputGroup className="input-group-alternative mb-3">
                          <InputGroupAddon addonType="prepend">
                            <InputGroupText>
                              <i className="ni ni-email-83" />
                            </InputGroupText>
                          </InputGroupAddon>
                          <Input
                            placeholder="Email"
                            type="email"
                            name="email"
                            value={form.email}
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
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            autoComplete="off"
                          />
                        </InputGroup>
                      </FormGroup>
                      <div className="text-center">
                        <Button className="mt-4" color="primary" type="submit">
                          Create account
                        </Button>
                      </div>
                    </Form>
                  </CardBody>
                </Card>
              </Col>
            </Row>
          </Container>
        </section>
      </main>
       {/* 여기서 모달 추가 */}
       <Modal
        isOpen={modalIsOpen}
        onRequestClose={() => setModalIsOpen(false)}
        contentLabel="Registration Error"
        style={{
          content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            border: 'none', // 테두리 색을 없앰
            boxShadow: '0px 4px 15px rgba(0, 0, 0, 0.2)', // 모달에 그림자 추가
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',  // 수직 방향 중앙 정렬
            justifyContent: 'center', // 수평 방향 중앙 정렬
          },
          overlay: {
            backgroundColor: 'rgba(0, 0, 0, 0.5)', // 배경에 반투명 효과 추가
          }
        }}
      >
        <div class="fontNeo" style={{margin:'20px'}}>{modalMessage}</div>
        <button type="button" className="btn-1 ml-1 btn btn-success" onClick={() => setModalIsOpen(false)}>Close</button>
      </Modal>
    </>
  );
};

export default Register;