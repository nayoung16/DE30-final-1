import React, { useState, useRef, useEffect } from 'react';
import { Button } from 'reactstrap';
import 'assets/css/argon-design-system-react.css';
import DemoNavbar from '../../components/Navbars/DemoNavbar';
import '../../assets/css/RecommandMain.css';
import '../../assets/css/fonts.css';
import '../../assets/css/campDetails.css';
import { useNavigate } from 'react-router-dom';
import { logoutUser } from 'api/logoutUser';
import { FaChevronLeft, FaChevronRight } from 'react-icons/fa';
import { updateUserAnswer } from '../../api/updateUserAnswer';
import { searchCamp } from '../../api/searchCamp'; // 검색 API 함수 가져오기

function RecommandMainPage({ isLogin, setIsLogin }) {
  const [answers, setAnswers] = useState({});
  const [questionIndex, setQuestionIndex] = useState(0);
  const [isInitialScreen, setIsInitialScreen] = useState(true);
  const [isResultScreen, setIsResultScreen] = useState(false);
  const [calculatedStyle, setCalculatedStyle] = useState('');
  const [campFacilities, setCampFacilities] = useState([]);
  const [searchResults, setSearchResults] = useState([]); // 검색 결과 상태 추가
  const [randomCamps, setRandomCamps] = useState([]); // 랜덤 캠핑장 상태 추가
  const mainRef = useRef(null);
  const navigate = useNavigate();

  const questions = [
    { id: 1, text: '캠핑 장비를 보유하고 계신가요?', options: ['없음0', '있음1'] },
    { id: 2, text: '편의시설의 여부가 중요한가요?', options: ['아니요0', '네1'] },
    { id: 3, text: '활동은 얼마나 하시나요?', options: ['안해요0', '조금해요1', '하는편이에요2', '많이 해요3'] },
    { id: 4, text: '동행은 누구와 함께하나요?', options: ['혼자0', '가족 또는 친구1'] },
    { id: 5, text: '특정 환경을 좋아하시는게 있나요?', options: ['예0', '아니요1'] },
    { id: 6, text: '이동수단은 무엇인가요?', options: ['차0', '오토바이1', '자전거2', '걸어서3'] },
    { id: 7, text: '편안함은 어떻게 생각하시나요?', options: ['모든게 갖춰짐0', '적당히 갖춰짐1', '최소한으로 갖춰짐2', '알아서 할수 있어요3'] },
    { id: 8, text: '주변환경은 무엇을 선호하시나요?', options: ['산', '숲', '해변', '도심'] },
    { id: 9, text: '어떤 활동을 즐기나요?', options: ['놀거리', '산책길', '꽃놀이', '액티비티'] },
    {
      id: 10, text: '어느 지역을 선호하시나요?', options: ['강원도', '경상북도', '경기도', '충청남도', '충청북도', '전라남도',
        '경상남도', '인천시', '전라북도', '대구시', '울산시', '제주도', '서울시', '세종시', '대전시', '부산시', '광주시']
    }
  ];

  useEffect(() => {
    if (!isLogin) {
      navigate('/login-page');
    }
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

  const handleStartQuestions = () => {
    setIsInitialScreen(false);
  };

  const handleNextQuestion = () => {
    setQuestionIndex((prevIndex) => prevIndex + 1);
  };

  const handlePreviousQuestion = () => {
    setQuestionIndex((prevIndex) => prevIndex - 1);
  };

  const handleAnswer = (answer) => {
    const questionId = questions[questionIndex].id;

    if (questionId >= 8) {
      // Store the selected answer as is for questions 8 and onward
      setAnswers({ ...answers, [questionIndex]: answer });
    } else {
      // Extract the number from the answer for questions 1 to 7
      const numericalAnswer = Number(answer.slice(-1)); // Extract the last digit
      setAnswers({ ...answers, [questionIndex]: numericalAnswer });
    }

    // Automatically navigate to the next question or submit if the last question
    if (questionIndex < questions.length - 1) {
      setQuestionIndex((prevIndex) => prevIndex + 1);
    } else {
      handleSubmit();
      console.log("핸들서브밋 성공");
    }
  };

  const handleSubmit = async () => {
    const userAnswerData = {
      gear_amount: answers[0],
      convenience_facility: answers[1],
      activity: answers[2],
      companion: answers[3],
      nature: answers[4],
      transport: answers[5],
      comfort: answers[6],
      envrn_filter: answers[7],
      thema_filter: answers[8],
      doNm: answers[9]
    };
    console.log(userAnswerData);
    try {
      const response = await updateUserAnswer(userAnswerData);

      // 스타일 계산
      const calculatedStyle = response.user.styleNm;
      setCalculatedStyle(calculatedStyle);

      // 시설 이름 가져오기
      const facilities = response.camps.map(camp => camp.facltNm);
      setCampFacilities(facilities);

      // 시설 이름을 기반으로 캠핑장 검색
      const allCamps = [];
      for (const facility of facilities) {
        const searchResponse = await searchCamp(facility);
        allCamps.push(...searchResponse);
      }

      // 검색 결과에서 5개의 랜덤 캠핑장 선택
      const randomCamps = getRandomCamps(allCamps, 5);
      setRandomCamps(randomCamps);

      setIsResultScreen(true);
    } catch (error) {
      console.error('사용자 답변 업데이트 실패', error);
    }
  };

  const handleProgressBarClick = () => {
    const progressPercentage = ((questionIndex + 1) / questions.length) * 100;
    if (progressPercentage >= 100) {
      handleSubmit();
    }
  };

  const handleGoHome = () => {
    setIsInitialScreen(true);
    setIsResultScreen(false);
    setAnswers({});
    setQuestionIndex(0);
  };

  const progressPercentage = ((questionIndex + 1) / questions.length) * 100;

  const selectedRegion = Object.values(answers)[9];
  const regionMap = {
    '0': '강원도',
    '1': '경상북도',
    '2': '경기도',
    '3': '충청남도',
    '4': '충청북도',
    '5': '전라남도',
    '6': '경상남도',
    '7': '인천시',
    '8': '전라북도',
    '9': '대구시',
    '10': '울산시',
    '11': '제주도',
    '12': '서울시',
    '13': '세종시',
    '14': '대전시',
    '15': '부산시',
    '16': '광주시'
  };
  const regionName = regionMap[selectedRegion];

  const chunkArray = (array, size) => {
    const result = [];
    for (let i = 0; i < array.length; i += size) {
      result.push(array.slice(i, i + size));
    }
    return result;
  };

  function getRandomCamps(camps, numberOfCamps) {
    const shuffled = [...camps].sort(() => 0.5 - Math.random());
    return shuffled.slice(0, numberOfCamps);
  }

  const styleImageMap = {
    '글램핑': require('assets/img/recommandImg/글램핑.png'),
    '모토캠핑': require('assets/img/recommandImg/모토캠핑.png'),
    '미니멀캠핑': require('assets/img/recommandImg/미니멀캠핑.png'),
    '백패킹': require('assets/img/recommandImg/백패킹.png'),
    '브롬핑': require('assets/img/recommandImg/브롬핑.png'),
    '오토캠핑': require('assets/img/recommandImg/오토캠핑.png'),
    '차박캠핑': require('assets/img/recommandImg/차박캠핑.png'),
    '캠프닉': require('assets/img/recommandImg/캠프닉.png'),
    '풀캠핑': require('assets/img/recommandImg/풀캠핑.png')
  };

  const styleDescriptionMap = {
    '오토캠핑': '차량을 이용해 캠핑장에 접근하여 캠핑을 즐기는 스타일. 많은 장비와 편의시설을 사용할 수 있음',
    '백패킹': '도보로 이동하며 필요한 장비를 배낭에 모두 넣어 자연 속에서 캠핑을 즐기는 스타일',
    '미니멀캠핑': '최소한의 장비로 간단하게 캠핑을 즐기는 스타일',
    '차박캠핑': '차량을 숙소로 활용하여 캠핑을 즐기는 스타일. 차량 안에서 잠을 자며 캠핑을 함',
    '캠프닉': '피크닉과 캠핑을 결합한 활동으로, 주로 짧은 시간 동안 야외에서 음식을 즐기고 간단한 캠핑을 함',
    '글램핑': '고급스럽고 편안한 시설을 갖춘 캠핑 스타일로, 일반적인 캠핑보다 편안함과 편리함을 제공함',
    '풀캠핑': '다양한 캠핑 장비와 편의시설을 갖추고 차량을 이용해 접근하는 캠핑 스타일',
    '모토캠핑': '오토바이를 이용해 이동하며 캠핑을 즐기는 스타일',
    '브롬핑': '자전거(특히 브롬톤)를 이용해 이동하며 캠핑을 즐기는 스타일'
  };

  return (
    <>
      <DemoNavbar isLogin={isLogin} setIsLogin={setIsLogin} />
      <main className="recommand-page" ref={mainRef}>
        <section className="section-profile-custom section-shaped my-0">
          <div className="shape shape-style-1 shape-custom">
            <span />
            <span />
            <span />
            <span />
            <span />
            <span />
          </div>
        </section>
        {isInitialScreen ? (
          <div className="recommand-initial-screen">
            <div className="recommand-text-top">
              <h1>캠핑 스타일 추천</h1>
              <p>자신에게 맞는 캠핑 스타일을 찾아보세요!</p>
            </div>
            <div className="recommand-image-container">
              <img
                src={require('assets/img/brand/camper.png')}
                alt="Initial"
                className="recommand-initial-image"
              />
            </div>
            <div className="recommand-text-bottom">
              <Button
                color="primary"
                onClick={handleStartQuestions}
                className="recommand-start-button"
              >
                질문 시작하기
              </Button>
            </div>
          </div>
        ) : isResultScreen ? (
          <div className="recommand-result-screen">
            <h1>당신의 캠핑 스타일</h1>
            <h3>{regionName}</h3>
            {calculatedStyle && (
              <div className="recommand-style-image">
                <img
                  src={styleImageMap[calculatedStyle]}
                  alt={calculatedStyle}
                  className="style-image"
                />
                <h2 className="style-name">{calculatedStyle}</h2>
                <p className="style-description">{styleDescriptionMap[calculatedStyle]}</p>
              </div>
            )}
            <div className="random-camps two-row">
              <h4>추천 캠핑장</h4>
              {randomCamps.map((camp) => (
              <div key={camp.contentId} className="camp-card">
                <img
                  src={camp.firstImageUrl || require('assets/img/brand/camper2.png')}
                  alt={camp.facltNm}
                  className={`camp-image ${!camp.firstImageUrl ? 'default-camp-image' : ''}`}
                />
                <h5 className="camp-name">{camp.facltNm}</h5>
                <p className="camp-address">{camp.addr1}</p>
                <Button onClick={() => window.open(`/camp-details/${camp.contentId}`, '_blank')}>
                  상세정보 보기
                </Button>
              </div>
            ))}
            </div>
            <Button
              onClick={handleGoHome}
              className="recommand-back-home-button"
            >
              홈으로 돌아가기
            </Button>
          </div>
        ) : (
          <>
            <div
              className="recommand-progress-bar"
              onClick={handleProgressBarClick}
              style={{ cursor: 'pointer' }}
            >
              <div
                className="recommand-progress-bar-inner"
                style={{ width: `${progressPercentage}%` }}
              />
            </div>

            <div className="recommand-question-container">
              <h1>{questions[questionIndex].text}</h1>

              {questionIndex > 0 && (
                <Button
                  color="secondary"
                  onClick={handlePreviousQuestion}
                  className="recommand-navigation-button recommand-previous-button"
                >
                  <FaChevronLeft />
                </Button>
              )}

              {questionIndex < questions.length - 1 && (
                <Button
                  color="secondary"
                  onClick={handleNextQuestion}
                  className="recommand-navigation-button recommand-next-button"
                >
                  <FaChevronRight />
                </Button>
              )}
            </div>

            <div className="recommand-options-container">
            {chunkArray(questions[questionIndex].options, 6).map((optionChunk, chunkIndex) => (
              <div className="recommand-options-row" key={chunkIndex}>
                {optionChunk.map((option, index) => {
                  // 1~7번 질문에만 마지막 문자를 제거
                  const text = questionIndex < 7 ? option.slice(0, -1) : option;
                  return (
                    <Button
                      key={index}
                      onClick={() => handleAnswer(option)}
                      className="recommand-option-button"
                    >
                        {text}
                      </Button>
                    );
                  })}
                </div>
              ))}
            </div>

            {questionIndex === questions.length - 1 && (
              <Button
                color="success"
                onClick={handleSubmit}
                className="recommand-submit-button mt-4"
              >
                제출하기
              </Button>
            )}
          </>
        )}
      </main>
    </>
  );
}

export default RecommandMainPage;
