// src/index.js

import "assets/vendor/nucleo/css/nucleo.css";
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './app';
import './assets/css/argon-design-system-react.css'; // CSS 파일 경로 수정
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>
);