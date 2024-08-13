import React, { useState, useRef, useEffect } from 'react';
import Calendar from 'react-calendar';
import DemoNavbar from 'components/Navbars/DemoNavbar';
import "assets/css/argon-design-system-react.css";
import 'react-calendar/dist/Calendar.css';
import 'assets/css/Calendar.css';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import moment from 'moment';
import { getUserInfo } from 'api/getUserInfo';
import { createSchedule, deleteSchedule, getAllSchedules, updateSchedule } from 'api/schedule';
import { searchCamp } from 'api/searchCamp';
import { getMiddleForecast, getShortForecast } from 'api/getWeather';
import { Button, Input, InputGroup, Modal, ModalBody, Spinner } from "reactstrap";
import StyledCalendar from "./CalendarPage/StyledCalendar"
import '../../assets/css/fonts.css';
import manycloud from '../../assets/img/icons/common/manycloud.png'; // 이미지 경로 임포트
import cloud from '../../assets/img/icons/common/cloud.png';
import rain from '../../assets/img/icons/common/rain.png';
import sun from '../../assets/img/icons/common/sun.png';
import sonagi from '../../assets/img/icons/common/sonagi.png';
import WeatherCard from './CalendarPage/WeatherCard';
// Styled components
const LayoutContainer = styled.div`
  display: flex;
  flex-direction: column; /* 수직 방향으로 레이아웃을 나눕니다. */
  height: 65vh; /* 전체 화면 높이로 설정 */
  padding: 20px;
`;
const MainContent = styled.div`
  display: flex;
  flex-direction: row;
  flex: 1; /* MainContent가 가능한 모든 공간을 차지하도록 설정 */
  padding: 20px;
  gap: 20px; /* Calendar와 RightSidebar 간의 간격을 조정 *  
  flex-wrap: wrap; /* 컬럼이 넘칠 경우 다음 줄로 이동 */
  margin-right : 100px;
  margin-left : 100px;
  height : 650px;
`;

const BottomSidebar = styled.div`
  display: flex;
  flex-direction: row; /* 컬럼을 수평으로 배치 */
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  height: auto; /* 높이는 자동으로 조정 */
  flex-wrap: wrap; /* 컬럼이 넘칠 경우 다음 줄로 이동 */
  margin-right : 100px;
  margin-left : 100px;
`;
const TopContainer = styled.div`
  margin-bottom: 20px; /* 아래와 간격 추가 */
`;
const SidebarColumn = styled.div`
  flex: 1;
  min-width: calc(100% / 7 - 20px); /* 7개의 섹션을 가로로 배열 */
  box-sizing: border-box; /* 패딩과 경계 포함하여 너비 계산 */
`;
const ColumnContent = styled.div`
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 10px;
  flex: none;
  width: 100%;
  display: flex;
  flex-direction: column;
`;

const TopSection = styled.div`
  background-color: #d0d0d0; /* 날짜가 들어갈 공간의 배경색 */
  flex: 1; /* 1 부분 */
  padding: 10px; /* 패딩 추가 */
  border-radius: 5px; /* 둥근 모서리 */
  text-align: center; /* 중앙 정렬 */
`;
const ImageSection = styled.div`
  background-color: #FFFFFF; /* 이미지가 들어갈 공간의 배경색 */
  flex: 8; /* 8 부분 */
  margin-top: 10px; /* 상단 여백 추가 */
  display: flex;
  border-radius: 5px; /* 둥근 모서리 */
  justify-content: center; /* 이미지 중앙 정렬 */
`;

const BottomSection = styled.div`
  display: flex;
  flex: 2; /* 8:2 비율의 2 부분 */
  margin-top: 10px;
`;

const BottomItem = styled.div`
  background-color: #e0e0e0; /* 각 하단 영역의 배경색 */
  text-align: center; /* 중앙 정렬 */
  flex: 1;
  margin-right: 10px;
  &:last-child {
    margin-right: 0;
    text-align: center; /* 중앙 정렬 */
  }
`;
const LastColumn = styled(SidebarColumn)`
  margin-right: 0; /* 마지막 컬럼의 오른쪽 여백 제거 */
`;

const RightSidebar = styled.div`
  flex: 1; /* RightSidebar가 MainContent 내에서 1배 크기 차지 */
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3); /* 그림자 추가 */
  border-radius: 10px;
  overflow: auto; /* 내용이 넘치면 스크롤 가능 */
`;

const CalendarContainer = styled.div`
  flex: 3; /* CalendarContainer가 MainContent 내에서 3배 크기 차지 */
  background-color: #fff;
  box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  overflow: auto; /* 내용이 넘치면 스크롤 가능 */
  height: 100%; /* CalendarContainer의 높이를 부모의 높이에 맞춤 */
`;

const ErrorText = styled.div`
  color: red;
  margin-top: 8px;
`;

const CampListItem = styled.li`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
`;

const SelectButton = styled(Button)`
  margin-left: auto;
  background-color: ${(props) => (props.selected ? '#c0c0c0' : '#f0f0f0')};
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
`;

const CenteredModal = styled(Modal)`
  .modal-dialog {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: calc(100% - 1rem);
  }

  .modal-content {
    border-radius: 0.5rem;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
`;
const ModalFooter = styled.div`
  padding: 10px;
  text-align: center;
`;
const Image = styled.img`
  max-width: 100%;
  max-height: 100%;
  border-radius: 5px; /* 이미지 모서리를 둥글게 */
`;
const LocationSelect = styled.select`
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  background-color: #fff;
`;

const weatherIconMap = {
  "맑음": sun,
  "구름많음" : manycloud,
  "구름많고 소나기" : sonagi,
  "흐리고 소나기" : sonagi,
  "비" : rain,
  "흐리고 비" : rain,
  "흐림" : cloud
}
const LocationSelector = ({ location, handleLocationChange }) => (
  <LocationSelect value={location} onChange={handleLocationChange}>  
    <option value="11D20501">강릉</option>
    <option value="11F20501">광주</option>
    <option value="21F10501">군산</option>
    <option value="11H10701">대구</option>
    <option value="11C20401">대전</option>
    <option value="21F20801">목포</option>
    <option value="11H20201">부산</option>
    <option value="11G00401">서귀포</option>
    <option value="11C20101">서산</option>
    <option value="11B10101">서울</option>
    <option value="11C20404">세종</option>
    <option value="11B20601">수원</option>
    <option value="11H10501">안동</option>
    <option value="11F20401">여수</option>
    <option value="11H20101">울산</option>
    <option value="11D10401">원주</option>
    <option value="11B20201">인천</option>
    <option value="11F10201">전주</option>
    <option value="11G00201">제주</option>
    <option value="11H20301">창원</option>
    <option value="11C10301">청주</option>  
    <option value="11D10301">춘천</option>
    <option value="11B20305">파주</option>
    <option value="11H10201">포항</option>
    {/* 다른 지역 옵션 추가 */}
  </LocationSelect>
);

const WeatherColumnContent = ({ date, location }) => {
  const [forecast, setForecast] = useState(null);
  const [hourlyData, setHourlyData] = useState([]);
  const [showWeatherModal, setShowWeatherModal] = useState(false);
  const [weatherDetails, setWeatherDetails] = useState(null);
  const [noDataMessage, setNoDataMessage] = useState(null);

  const weatherIconStyle = {
    width: "96px",
    height: "96px",
  };

  useEffect(() => {
    const fetchForecast = async () => {
      const data = await getMiddleForecast(date.format("YYYYMMDD"));

      if (data && Array.isArray(data)) {
        const locationForecast = data.find((item) => item.regTempId === location);
        setForecast(locationForecast || null);
      }
    };
    if (location) {
      fetchForecast();
    }
  }, [date, location]);

  const handleWeatherClick = async () => {
    const hourlyForecast = await getShortForecast(date.format("YYYYMMDD"));
    if (hourlyForecast && Array.isArray(hourlyForecast)) {
      const filteredHourlyData = hourlyForecast.filter(item => item.regId === location);
      if (filteredHourlyData.length === 0) {
        // If no data is found, set no data message
        setNoDataMessage("수집되지 않은 데이터입니다");
        setHourlyData([]);
      } else {
        const sortedHourlyData = filteredHourlyData
          .map(item => ({
            ...item,
            fcstTime: parseInt(item.fcstTime, 10)
          }))
          .sort((a, b) => a.fcstTime - b.fcstTime);
        setHourlyData(sortedHourlyData);
        setNoDataMessage(null);
      }
    } else {
      setNoDataMessage("수집되지 않은 데이터입니다");
      setHourlyData([]);
    }
    setWeatherDetails(forecast);
    setShowWeatherModal(true);
  };

  const toggleWeatherModal = () => {
    setShowWeatherModal(!showWeatherModal);
  };

  const currentHour = moment().hour();
  const isMorning = currentHour < 12;
  const weatherCondition = forecast ? (isMorning ? forecast.wfAm : forecast.wfPm) : 'N/A';

  return (
    <div>
      <ColumnContent onClick={handleWeatherClick}>
        <TopSection>{date.format("YYYY-MM-DD")}</TopSection>
        <ImageSection>
          <img
            src={weatherIconMap[weatherCondition]}
            style={weatherIconStyle}
            alt={`Weather ${date.format("YYYY-MM-DD")}`}
          />
        </ImageSection>
        <BottomSection>
          <BottomItem>최고: {forecast?.taMax ?? "N/A"}°C</BottomItem>
          <BottomItem>최저: {forecast?.taMin ?? "N/A"}°C</BottomItem>
        </BottomSection>
      </ColumnContent>

      <Modal 
        isOpen={showWeatherModal} 
        toggle={toggleWeatherModal} 
        size="lg"
      >
        <ModalBody style={{ padding: '0' }}>
          {noDataMessage ? (
            <div style={{ padding: '20px', textAlign: 'center' , fontSize: '24px', fontWeight: 'fontNB'}}>{noDataMessage}</div>
          ) : weatherDetails && hourlyData.length > 0 ? (
            <WeatherCard 
              date={date} 
              weatherDetails={weatherDetails} 
              hourlyData={hourlyData} 
            />
          ) : (
            <div>Loading...</div>
          )}
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggleWeatherModal}>
            Close
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

function CalendarPage({ isLogin, setIsLogin }) {
  const navigate = useNavigate();
  const mainRef = useRef(null);
  const [date, setDate] = useState(new Date());
  const [events, setEvents] = useState({});
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [campName, setCampName] = useState('');
  const [campList, setCampList] = useState([]);
  const [showScheduleModal, setShowScheduleModal] = useState(false);
  const [modalDate, setModalDate] = useState(null);
  const [location, setLocation] = useState('11B10101');
  const handleLocationChange = (e) => {
    setLocation(e.target.value);
  }
  const [newSchedule, setNewSchedule] = useState({
    fromDate: '',
    toDate: '',
    content: '',
    contentId: ''
  });
  const [info, setInfo] = useState({});
  const [schedules, setSchedules] = useState([]);
  const [selectedScheduleDetails, setSelectedScheduleDetails] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isAdding, setIsAdding] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [selectedCampId, setSelectedCampId] = useState(null);
  const [isLoading, setIsLoading] = useState(false); // Loading state for modal
  const [operationType, setOperationType] = useState(null); // 'update' or 'delete'
  

  useEffect(() => {
    const fetchSchedules = async () => {
      const schedules = await getAllSchedules();
      if (schedules) {
        setSchedules(schedules);
      }
    };

    fetchSchedules();
  }, []);

  useEffect(() => {
    const fetchUserInfo = async () => {
      const userInfo = await getUserInfo();
      setInfo(userInfo);
    };

    fetchUserInfo();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewSchedule((prevSchedule) => ({ ...prevSchedule, [name]: value }));
  };

  const handleCreateSchedule = async () => {
    const userInfo = await getUserInfo();
    if (!userInfo) {
      alert('로그인 해주세요');
      navigate('/login');
      return;
    }

    setIsLoading(true);
    const createdSchedule = await createSchedule(newSchedule);
    if (createdSchedule) {
      setSchedules((prevSchedules) => [...prevSchedules, createdSchedule]);
      setNewSchedule({ fromDate: '', toDate: '', content: '', contentId: '' });
      setShowScheduleModal(true);
      setOperationType('create'); // For create operation
      setIsLoading(false);
    }
  };

  const handleUpdateSchedule = async () => {
    const updatedSchedule = await updateSchedule(selectedScheduleDetails.schId, newSchedule);
    if (updatedSchedule) {
      setSchedules((prevSchedules) =>
        prevSchedules.map((schedule) =>
          schedule.schId === updatedSchedule.schId ? updatedSchedule : schedule
        )
      );
      setSelectedScheduleDetails(updatedSchedule);
      setIsEditing(false);
      setShowScheduleModal(true); // Show the modal
      setOperationType('update'); // Set operation type to 'update'
    }
  };
 
  const handleDeleteSchedule = async (schId) => {
    const deleted = await deleteSchedule(schId);
    if (deleted) {
      setSchedules((prevSchedules) =>
        prevSchedules.filter((schedule) => schedule.schId !== schId)
      );
      setSelectedScheduleDetails(null);
      setNewSchedule({ fromDate: '', toDate: '', content: '', contentId: '' });
      setIsEditing(false);
      setShowScheduleModal(true); // Show the modal
      setOperationType('delete'); // Set operation type to 'delete'
    }
  };
  
  const handleSearchCamp = async () => {
    if (campName.trim() === '') {
      setErrorMessage('캠핑장을 입력해주세요.');
      return;
    }

    setErrorMessage('');
    const results = await searchCamp(campName);
    setCampList(results);
  };

  const handleSelectCamp = (contentId) => {
    setNewSchedule((prevSchedule) => ({ ...prevSchedule, contentId }));
    setSelectedCampId(contentId);
  };

  const handleDayClick = (date) => {
    const formattedDate = moment(date).format("YYYY-MM-DD");
    const dayEvents = schedules.filter(schedule => schedule.fromDate <= formattedDate && schedule.toDate >= formattedDate);
    if (dayEvents.length > 0) {
      setSelectedEvent(dayEvents);
      setIsEditing(true);
      setIsAdding(false);
    } else {
      setSelectedEvent(null);
      setIsEditing(false);
      setIsAdding(true);
      setNewSchedule({ ...newSchedule, fromDate: formattedDate, toDate: formattedDate });
    }
  };

  const handleEventClick = (event) => {
    setSelectedScheduleDetails(event);
    setNewSchedule({
      fromDate: event.fromDate,
      toDate: event.toDate,
      content: event.content,
      contentId: event.contentId
    });
    setIsEditing(true);
    setIsAdding(false);
  };

  const toggleModal = () => {
    setShowScheduleModal(!showScheduleModal);
    setOperationType(null); // Reset operation type when closing modal
  };
  

  return (
    <>
      <DemoNavbar isLogin={isLogin} setIsLogin={setIsLogin} />
      <section className="section-profile-custom section-shaped my-0">
        <div className="shape shape-style-1 shape-custom">
          <span />
          <span />
          <span />
          <span />
          <span />
          <span />
          <span />
        </div>
      </section>
      <LayoutContainer ref={mainRef}>
        <MainContent>
          <CalendarContainer>
            <StyledCalendar
              onChange={setDate}
              value={date}
              onClickDay={handleDayClick}
              next2Label={null}
              prev2Label={null}
              locale="en-US"
              formatDay={(locale, date) => moment(date).format("D")}
              formatYear={(locale, date) => moment(date).format("YYYY")}
              minDetail="year"
              tileContent={({ date, view }) => {
                const formattedDate = moment(date).format("YYYY-MM-DD");
                const dayEvents = schedules.filter(schedule => schedule.fromDate <= formattedDate && schedule.toDate >= formattedDate);
                return (
                  <>
                    {dayEvents.map((event, index) => {
                      const isStart = event.fromDate === formattedDate;
                      const isEnd = event.toDate === formattedDate;
                      return (
                        <div
                          key={index}
                          className={`event-highlight ${!isStart && !isEnd ? 'middle' : ''}`}
                          onClick={() => handleEventClick(event)}
                        >
                          {event.content}
                        </div>
                      );
                    })}
                  </>
                );
              }}
            />
          </CalendarContainer>
          <RightSidebar>
            {isAdding && (
              <>
                <h5 className="fontNB">일정 추가</h5>
                <div style={{ marginTop: '16px' }}>
                  <InputGroup>
                    <Input
                      type="date"
                      name="fromDate"
                      value={newSchedule.fromDate}
                      onChange={handleInputChange}
                    />
                    <Input
                      type="date"
                      name="toDate"
                      value={newSchedule.toDate}
                      onChange={handleInputChange}
                    />
                  </InputGroup>
                </div>
                <h5 className="fontNB" style={{ marginTop: '16px' }}>메모</h5>
                <Input
                  type="textarea"
                  name="content"
                  placeholder="일정 내용을 입력하세요"
                  value={newSchedule.content}
                  onChange={handleInputChange}
                  style={{ marginTop: '16px' }}
                />
                <h5 className="fontNB" style={{ marginTop: '16px' }}>캠핑장 검색</h5>
                <InputGroup>
                  <Input
                    type="text"
                    placeholder="캠핑장 이름을 입력하세요"
                    value={campName}
                    onChange={(e) => setCampName(e.target.value)}
                  />
                  <Button color="success" onClick={handleSearchCamp} className="fontNB" style={{ marginLeft: '2px' }}>검색</Button>
                </InputGroup>
                {errorMessage && <ErrorText>{errorMessage}</ErrorText>}
                <h5 className="fontNB" style={{ marginTop: '16px' }}>검색 내용</h5>
                <ul>
                  {campList.map((camp) => (
                    <CampListItem key={camp.contentId}>
                      {camp.facltNm}
                      <SelectButton
                        color="secondary"
                        selected={selectedCampId === camp.contentId}
                        onClick={() => handleSelectCamp(camp.contentId)}
                        className="fontNB"
                      >
                        선택
                      </SelectButton>
                    </CampListItem>
                  ))}
                </ul>
                <ButtonContainer>
                  <Button color="btn-1 ml-1 btn btn-success" onClick={handleCreateSchedule} className="fontNB">
                    추가
                  </Button>
                </ButtonContainer>
              </>
            )}
            {isEditing && selectedScheduleDetails && (
              <>
                <h5 className="fontNB">일정 수정</h5>
                <div style={{ marginTop: '16px' }}>
                  <InputGroup>
                    <Input
                      type="date"
                      name="fromDate"
                      value={newSchedule.fromDate}
                      onChange={handleInputChange}
                    />
                    <Input
                      type="date"
                      name="toDate"
                      value={newSchedule.toDate}
                      onChange={handleInputChange}
                    />
                  </InputGroup>
                </div>
                <div style={{ marginTop: '16px' }}>
                  <h5 className="fontNB">메모</h5>
                </div>
                <div style={{ marginTop: '16px' }}>
                  <Input
                    type="textarea"
                    name="content"
                    placeholder="일정 내용을 입력하세요"
                    value={newSchedule.content}
                    onChange={handleInputChange}
                  />
                </div>
                <div style={{ marginTop: '16px' }}>
                  <Button color="success" onClick={handleUpdateSchedule}>수정</Button>
                  <Button color="danger" onClick={() => handleDeleteSchedule(selectedScheduleDetails.schId)}>삭제</Button>
                </div>
              </>
            )}
          </RightSidebar>
        </MainContent>
        <BottomSidebar>
          <LocationSelector location={location} handleLocationChange={handleLocationChange} />
          {[...Array(7)].map((_, index) => (
            <SidebarColumn key={index}>
              <WeatherColumnContent date={moment().add(index, "days")} location={location}/>
            </SidebarColumn>
          ))}
        </BottomSidebar>
        </LayoutContainer>
      <CenteredModal isOpen={showScheduleModal} toggle={toggleModal}>
        <ModalBody>
          {isLoading ? (
            <Spinner />
          ) : (
            (operationType === 'update' ? '일정이 수정되었습니다.' : operationType === 'delete' ? '일정이 삭제되었습니다.' : '일정이 추가되었습니다.')
          )}
        </ModalBody>
      </CenteredModal>
    </>
  );
}

export default CalendarPage;
