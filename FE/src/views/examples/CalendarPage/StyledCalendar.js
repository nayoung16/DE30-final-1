import styled from 'styled-components';
import Calendar from 'react-calendar';

const StyledCalendar = styled(Calendar)`
  width: 2000px;
  height: 600px;
  background: white;
  border: none; /* Border 제거 */
  font-family: Arial, Helvetica, sans-serif;
  line-height: 1.125em;
  border-radius: 15px;
  padding: 20px;
  box-sizing: border-box;
  box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.3); /* 그림자 추가 */

  .react-calendar__navigation {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    margin-bottom: 1em;
    position: relative;
  }

  .react-calendar__navigation button {
    border: none;
    background: none;
    font-size: 1.2em;
    color: #333;
    padding: 0;
    margin: 0 0.3em;
  }

  .react-calendar__navigation button:focus {
    outline: none;
  }

  .react-calendar__navigation__label {
    display: flex;
    justify-content: center;
    align-items: center;
    width: auto;
    font-size: 1.2em;
  }

  .react-calendar__navigation__label__text {
    font-size: 1em;
  }

  .react-calendar__navigation__label__text {
    font-size: 1.2em;
    margin: 0;
  }

  .react-calendar__navigation__arrow {
    border: none;
    background: none;
    font-size: 1.5em;
    color: #007bff;
  }

  .react-calendar__navigation__arrow--prev {
    margin-right: 10px;
  }

  .react-calendar__navigation__arrow--next {
    margin-left: 10px;
  }

  .react-calendar__navigation__label__year {
    display: none; /* 연도 선택 숨기기 */
  }

  .react-calendar__navigation__label__month {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1.2em;
  }

  .react-calendar__tile {
    border-radius: 4px;
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px;
    box-sizing: border-box;
    height: 95px; /* height 줄이기 */
  }

  .react-calendar__tile__date {
    font-size: 1.2em;
    font-weight: bold;
    margin-bottom: 5px;
  }

  .event-highlight {
    position: absolute;
    background-color: #e0f7e0;
    color: #333;
    padding: 2px;
    border-radius: 2px;
    font-size: 0.8em;
    width: 100%;
    text-align: center;
    box-sizing: border-box;
    bottom: 5px;
  }

  .event-highlight.middle {
    color: #e0f7e0; /* 중간 타일의 텍스트 색상을 배경색과 동일하게 설정 */
  }
`;

export default StyledCalendar;
