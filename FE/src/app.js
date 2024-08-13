import "assets/scss/argon-design-system-react.scss?v1.1.0";
import "assets/vendor/font-awesome/css/font-awesome.min.css";
import "assets/vendor/nucleo/css/nucleo.css";
import { Suspense, useEffect, useState, useTransition } from 'react';
import { Navigate, Route, Routes, useLocation } from "react-router-dom";
import { getUserInfo } from './api/getUserInfo';

import Index from "views/Index.js";
import Calendar from "views/examples/Calendar.js";
import CampDetails from "views/examples/CampDetails.js";
import Login from "views/examples/Login.js";
import Profile from "views/examples/Profile.js";
import Register from "views/examples/Register.js";
import Search from "views/examples/Search.js";

import DemoNavbar from 'components/Navbars/DemoNavbar';
import { NavermapsProvider } from 'react-naver-maps';

import RecommandMainPage from 'views/examples/RecommandMain.js';

export default function App() {
  const [isLogin, setIsLogin] = useState(false);
  const [isPending, startTransition] = useTransition();
  const location = useLocation();

  useEffect(() => {
    startTransition(async () => {
      const name = await getUserInfo();
      setIsLogin(!!name);
    });
  }, []);

  useEffect(() => {
    // 페이지 변경 시 토글 메뉴 닫기
    const closeNavbar = () => {
      const navbarToggler = document.querySelector('.navbar-toggler');
      const collapse = document.querySelector('.navbar-collapse');
      if (collapse.classList.contains('show')) {
        navbarToggler.click();
      }
    };
    closeNavbar();
  }, [location]);

  return (
    <NavermapsProvider ncpClientId={process.env.REACT_APP_NAVER_MAPS_CLIENT_ID}>
      <div className="App">
        <DemoNavbar isLogin={isLogin} setIsLogin={setIsLogin} />
        <Suspense fallback={<div>Loading...</div>}>
          <Routes>
            <Route path="/" element={<Index isLogin={isLogin} setIsLogin={setIsLogin} />} />
            <Route path="/search-page" element={<Search isLogin={isLogin} setIsLogin={setIsLogin} />} />
            <Route path="/camp-details/:id" element={<CampDetails />} />
            <Route path="/login-page" element={<Login isLogin={isLogin} setIsLogin={setIsLogin} />} />
            <Route path="/profile-page" element={isLogin ? <Profile isLogin={isLogin} setIsLogin={setIsLogin} /> : <Navigate to="/login-page" replace />} />
            <Route path="/camp-details/:id" element={<CampDetails />} />
            <Route path="/calendar-page" element={isLogin ? <Calendar isLogin={isLogin} setIsLogin={setIsLogin} /> : <Navigate to="login-page" replace />} />
            <Route path="/register-page" element={<Register />} />
            <Route path="/recommand-page" element={<RecommandMainPage isLogin={isLogin} setIsLogin={setIsLogin} />} />
            <Route path="/camp-details/:id" element={<CampDetails />} />
            <Route path="/" element={<Index isLogin={isLogin} setIsLogin={setIsLogin} />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Suspense>
      </div>
    </NavermapsProvider>
  );
}
