import { getUserFavorites } from "api/getUserFavorites";
import { getUserInfo } from "api/getUserInfo";
import { logoutUser } from "api/logoutUser";
import { removeUserFavorite } from "api/removeUserFavorites";
import { updateUserInfo } from "api/updateUserInfo";
import { uploadImage } from "api/uploadImage";
import "assets/css/argon-design-system-react.css";
import DemoNavbar from "components/Navbars/DemoNavbar.js";
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from "react-router-dom";
import { Button, Card, Col, Container, Modal, Row } from "reactstrap";

export default function Profile({ isLogin, setIsLogin }) {
  const navigate = useNavigate();
  const mainRef = useRef(null);
  const [info, setInfo] = useState({});
  const [styleNm, setStyleNm] = useState("");
  const [modal, setModal] = useState(false);
  const [nickNameModal, setNickNameModal] = useState(false);
  const [newImage, setNewImage] = useState("");
  const [newNickName, setNewNickName] = useState("");
  const [loading, setLoading] = useState(false);
  const [favorites, setFavorites] = useState([]);
  
  const IconsMap = {
    글램핑: require("assets/img/recommandImg/글램핑.png"),
    모토캠핑: require("assets/img/recommandImg/모토캠핑.png"),
    미니멀캠핑: require("assets/img/recommandImg/미니멀캠핑.png"),
    백패킹: require("assets/img/recommandImg/백패킹.png"),
    브롬핑: require("assets/img/recommandImg/브롬핑.png"),
    오토캠핑: require("assets/img/recommandImg/오토캠핑.png"),
    차박캠핑: require("assets/img/recommandImg/차박캠핑.png"),
    캠프닉: require("assets/img/recommandImg/캠프닉.png"),
    풀캠핑: require("assets/img/recommandImg/풀캠핑.png"),
  };
  useEffect(() => {
    if (!isLogin) {
      navigate('/login-page');
    }

    const initUserInfo = async () => {
      try {
        const newInfo = await getUserInfo();
        setInfo(newInfo);
        if (newInfo) {
          await setStyleNm(newInfo.styleNm);
        }
        const userFavorites = await getUserFavorites();
        setFavorites(userFavorites);
      } catch (error) {
        console.error('Failed to fetch user info or favorites', error);
      }
    };
    initUserInfo();
  }, [isLogin, navigate]);

  useEffect(() => {
    document.documentElement.scrollTop = 0;
    document.scrollingElement.scrollTop = 0;
    if (mainRef.current) {
      mainRef.current.scrollTop = 0;
    }
  }, []);

  const handleLogout = async () => {
    try {
      const result = await logoutUser();
      if (result) {
        setIsLogin(false);
        navigate('/');
      } else {
        alert('Logout failed.');
      }
    } catch (error) {
      console.error('Logout failed', error);
    }
  };

  const handleDetailsClick = (contentId) => {
    navigate(`/camp-details/${contentId}`);
  };

  const handleImageChange = (e) => {
    setNewImage(e.target.files[0]);
  };

  const handleImageUpdate = async () => {
    if (!newImage) {
      alert('이미지를 선택하세요');
      return;
    }

    setLoading(true);

    try {
      const { imageUrl } = await uploadImage(newImage);
      const updatedUserInfo = { ...info, userImg: imageUrl };
      const updatedInfo = await updateUserInfo(updatedUserInfo);
      setInfo(updatedInfo);
      setModal(false);
    } catch (error) {
      console.error('Failed to update image', error);
      alert('이미지 업데이트에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const handleRemoveFavorite = async (contentId) => {
    try {
      const result = await removeUserFavorite(contentId);
      if (result && result.message === "Favorite removed successfully") {
        setFavorites(favorites.filter(fav => fav.contentId !== contentId));
        alert("성공적으로 즐겨찾기를 삭제하였습니다")
      } else {
        alert('Failed to remove favorite.');
      }
    } catch (error) {
      console.error('Failed to remove favorite', error);
    }
  };

  const calendarPage = () => {
    navigate('/calendar-page')
  }

  const handleNickNameClick = () => {
    setNewNickName(info.nickName);
    setNickNameModal(true);
  };

  const handleNickNameUpdate = async () => {
    if (!newNickName) {
      alert('닉네임을 입력하세요');
      return;
    }

    setLoading(true);

    try {
      const updatedUserInfo = { ...info, nickName: newNickName };
      const updatedInfo = await updateUserInfo(updatedUserInfo);
      setInfo(updatedInfo);
      setNickNameModal(false);
    } catch (error) {
      console.error('Failed to update nickname', error);
      alert('닉네임 업데이트에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <DemoNavbar isLogin={isLogin} setIsLogin={setIsLogin} />
      <main className="profile-page" ref={mainRef}>
        <section className="section-profile-cover section-shaped my-0">
          <div className="shape shape-style-1 shape-custom">
            <span />
            <span />
            <span />
            <span />
            <span />
            <span />
            <span />
          </div>
          <div className="separator separator-bottom separator-skew">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              preserveAspectRatio="none"
              version="1.1"
              viewBox="0 0 2560 100"
              x="0"
              y="0"
            >
              <polygon className="fill-white" points="2560 0 2560 100 0 100" />
            </svg>
          </div>
        </section>
        <section className="section">
          <Container>
            <Card className="card-profile shadow mt--300" style={{ borderRadius: '15px' }}>
              <section>
                    <div className="card-profile-image">
                      <a href="#pablo" onClick={(e) => e.preventDefault()}>
                        <img
                          alt="..."
                          className="rounded-circle"
                          src={info.userImg || require("assets/img/brand/camper3.png")}
                          onClick={() => setModal(true)}
                          style={{ width: '150px', height: '150px', objectFit: 'cover' }}
                        />
                      </a>
                    </div>
              </section>
              <section className="text-center">
                <div className="card-profile-stats d-flex justify-content-between mx-sm-4">
                  <div className="d-flex flex-column align-items-center">
                  <div d-flex flex-column style={{backgroundColor:"#2DCE89", borderRadius: '15px', padding: '10px',
                      width: '80px'
                    }}>
                      <span className="heading" style={{color:"white"}}>{favorites.length}</span>
                      <span className="description mt-1" style={{color:"white"}}>Favorites</span>
                    </div>
                  </div>
                  <div className="d-flex align-items-center flex-profile-gap">
                    {isLogin && (
                      <Button className="mr-2" onClick={handleLogout} size="sm">
                        {/* <span className="btn-inner--icon"><i className="ni ni-user-run" /></span> */}
                        <span className="btn-inner--text">Logout</span>
                      </Button>
                    )}
                    <Button className="mr-2" href="#pablo" onClick={calendarPage} size="sm">
                    {/* <span className="btn-inner--icon"><i className="ni ni-user-run" /></span> */}
                    <span className="btn-inner--text">Calendar</span>
                    </Button>
                    
                  </div>
                </div>
                <div style={{marginTop:"10px",justifyContent:"center", display: 'flex', gap:"1%"}}>
                  <h3  class="fontNB" style={{marginLeft:"10px"}}>
                      {info.nickName}{" "}
                  </h3>
                  <h3>
                  <img width="20" height="20" src="https://img.icons8.com/windows/32/edit--v1.png" alt="edit--v1"
                      onClick={handleNickNameClick} style={{ cursor: 'pointer' }}/>
                  </h3>
                </div>
                      {/* <div className="h6 font-weight-300">
                        <i className="ni location_pin mr-2" />
                        {info.id}
                      </div> */}
              </section>
              <div className="px-4">
                <Row className="justify-content-center">
                  <Col lg="7">
                <section>
                  <div className="px-4">
                    <Row className="justify-content-center">
                      <Col lg="7">
                        <section>
                          <div className="text-center">
                            <div className="h6 mt-4">
                              <h5 className="fontNeo">나의 캠핑 스타일</h5>
                              <img 
                                  src={IconsMap[styleNm]}
                                  alt="캠핑 스타일 이미지"
                                  style={{
                                    borderRadius: "10px",
                                    marginRight: "15px",
                                    width: "100px",
                                    height: "100px",
                                    objectFit: "cover"
                                  }} 
                                />
                              <div 
                                style={{ 
                                  display: "flex", 
                                  alignItems: "center", 
                                  justifyContent: "center", 
                                  margin: "20px auto" 
                                }}
                              >
                                <ul
                                  style={{
                                    backgroundColor: "#F5F1EF",
                                    borderRadius: "10px",
                                    paddingLeft: "15px",
                                    paddingRight: "10px",
                                    paddingTop: "10px",
                                    paddingBottom: "10px",
                                    width: "50%",
                                    display: "flex", // 플렉스 박스로 설정
                                    alignItems: "center", // 수직 중앙 정렬
                                    justifyContent: "center", // 수평 중앙 정렬
                                    textAlign: "center", // 텍스트 중앙 정렬
                                  }}
                                >
                                  {styleNm || "캠핑스타일이 없습니다"}
                                </ul>
                              </div>
                            </div>
                          </div>
                        </section>
                      </Col>
                    </Row>
                  </div>
                </section>
                  <div className="text-center mt-5">
                    <h5 class="fontNeo">캠핑 즐겨찾기 목록</h5>
                    <ul className="list-unstyled">
                      {Array.isArray(favorites) ? (
                        favorites.map(fav => (
                          <li
                            key={fav.contentId}
                            className="d-flex justify-content-between align-items-center py-2 border-bottom"
                            style={{
                              backgroundColor: "#F5F1EF",
                              borderRadius: "10px",
                              marginBottom: "20px",
                              paddingLeft: "10px",
                              paddingRight: "10px",
                              paddingTop: "10px",
                              paddingBottom: "10px",
                            }}
                          >
                              <div style={{ textAlign: "left", marginLeft:"10px"}}>
                                <button type="button" style={{marginTop: "5px", border:"none"}} 
                                        onClick={() => handleDetailsClick(fav.contentId)}>
                                    <strong>{fav.facltNm}</strong>
                                </button>
                                <br />
                                <i className="ni ni-pin-3"></i>&nbsp;&nbsp;{fav.addr1}
                              </div>
                            <Button className="rounded-circle" color="Secondary" type="button" onClick={() => handleRemoveFavorite(fav.contentId)}>
                              <span className="btn-inner--icon"><i className="ni ni-fat-delete" /></span>
                            </Button>
                          </li>
                        ))
                      ) : (
                        <li>즐겨찾기 목록이 없습니다.</li>
                      )}
                    </ul>
                  </div>
                  </Col>
                </Row>
              </div>
            </Card>
          </Container>
        </section>
      </main>

      <Modal className="modal-dialog-centered" isOpen={modal} toggle={() => setModal(!modal)}>
        <div className="modal-header">
          <h5 className="modal-title" id="exampleModalLabel">
            프로필 이미지 변경
          </h5>
          <button
            aria-label="Close"
            className="close"
            type="button"
            onClick={() => setModal(!modal)}
          >
            <span aria-hidden={true}>×</span>
          </button>
        </div>
        <div className="modal-body">
          <input type="file" onChange={handleImageChange} className="form-control" />
        </div>
        <div className="modal-footer">
          <Button color="secondary" data-dismiss="modal" type="button" onClick={() => setModal(!modal)}>
            닫기
          </Button>
          <Button color="primary" type="button" onClick={handleImageUpdate} disabled={loading}>
            {loading ? '업데이트 중...' : '업데이트'}
          </Button>
        </div>
      </Modal>

      <Modal className="modal-dialog-centered" isOpen={nickNameModal} toggle={() => setNickNameModal(!nickNameModal)}>
        <div className="modal-header">
          <h5 className="modal-title" id="exampleModalLabel">
            닉네임 변경
          </h5>
          <button
            aria-label="Close"
            className="close"
            type="button"
            onClick={() => setNickNameModal(!nickNameModal)}
          >
            <span aria-hidden={true}>×</span>
          </button>
        </div>
        <div className="modal-body">
          <input type="text" value={newNickName} onChange={(e) => setNewNickName(e.target.value)} className="form-control" />
        </div>
        <div className="modal-footer">
          <Button color="secondary" data-dismiss="modal" type="button" onClick={() => setNickNameModal(!nickNameModal)}>
            닫기
          </Button>
          <Button color="primary" type="button" onClick={handleNickNameUpdate} disabled={loading}>
            {loading ? '업데이트 중...' : '업데이트'}
          </Button>
        </div>
      </Modal>
    </>
  );
}
