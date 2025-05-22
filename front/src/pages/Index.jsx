import React, { useState, useEffect } from 'react';
import {useLocation, useNavigate} from "react-router-dom";

const Index = () => {

  const baseUrl = process.env.REACT_APP_API_URL
  const navigate = useNavigate()
  const location = useLocation()

  // 토큰 유효성 검사 함수
  const isValidToken = (token) => {
    if (!token) return false;

    try {
      // JWT 토큰 형식 확인 (3개 부분으로 나누어져 있는지)
      const parts = token.split('.');
      if (parts.length !== 3) return false;

      // payload 디코딩해서 만료시간 확인
      const payload = JSON.parse(atob(parts[1]));
      const currentTime = Math.floor(Date.now() / 1000);

      // 토큰이 만료되지 않았는지 확인
      return payload.exp && payload.exp > currentTime;
    } catch (error) {
      console.error('토큰 검증 오류:', error);
      return false;
    }
  };


  // 컴포넌트 마운트 시 기존 토큰 확인
  useEffect(() => {
    const existingToken = localStorage.getItem('accessToken');
    if (isValidToken(existingToken)) {
      // 유효한 토큰이 있으면 바로 PubSearch로 이동
      navigate('/PubSearch');
    } else {
      // 무효한 토큰이면 제거
      if (existingToken) {
        localStorage.removeItem('accessToken');
      }
    }
  }, [navigate]);

  // URL에서 토큰 추출 및 처리
  useEffect(() => {
    const urlParams = new URLSearchParams(location.search);
    const token = urlParams.get('token');

    if(token) {
      if (isValidToken(token)) {
        localStorage.setItem('accessToken', token);

        // URL에서 토큰 파라미터 제거
        window.history.replaceState({}, document.title, '/');

        navigate('/PubSearch');
      } else {
        console.error('유효하지 않은 토큰입니다.');
        alert('로그인 중 오류가 발생했습니다. 다시 시도해주세요.');
      }
    }
  }, [location, navigate]);

  return (
    <div className="container" style={{
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      height: '100vh',
      width: '60vw',
      maxWidth: '960px',
      margin: '0 auto',
      backgroundColor: '#f2f2f2'
    }}>
      <div className="left-div" style={{
        flex: 1,
        padding: '20px',
        backgroundColor: '#f9f9f9',
        textAlign: 'center',
        width: '60%'
      }}>
        <img src="/images/beer-advertising.jpg" alt="Image" style={{height: '600px', width: '400px', maxWidth: '100%'}}/>
      </div>

      <div>
        <a href={baseUrl+"/oauth2/authorization/kakao"} role="button" id="kakao-login" className="me-2">
          <img alt="Kakao Login" src="/images/kakao_login_medium.png"/>
        </a>
      </div>
    </div>
  );
};

export default Index;