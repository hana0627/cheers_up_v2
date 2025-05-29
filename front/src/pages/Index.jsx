import React, { useEffect } from 'react';
import { useLocation, useNavigate } from "react-router-dom";
import { useAuth } from './context/AuthContext';

const Index = () => {
  const baseUrl = process.env.REACT_APP_API_URL;
  const navigate = useNavigate();
  const location = useLocation();
  const { login, isAuthenticated, loading } = useAuth();

  // 이미 인증된 사용자는 PubSearch로 리다이렉트
  useEffect(() => {
    if (!loading && isAuthenticated) {
      navigate('/PubSearch');
    }
  }, [isAuthenticated, loading, navigate]);

  // URL에서 토큰 추출 및 로그인 처리
  useEffect(() => {
    const urlParams = new URLSearchParams(location.search);
    const token = urlParams.get('token');

    if (token) {
      // 로그인 시도
      const success = login(token);

      // URL에서 토큰 파라미터 제거 (보안상 중요)
      window.history.replaceState({}, document.title, '/');

      if (success) {
        // 로그인 성공 시 원래 가려던 페이지로 이동 (또는 기본 페이지)
        const from = location.state?.from?.pathname || '/PubSearch';
        navigate(from, { replace: true });
      } else {
        alert('로그인 중 오류가 발생했습니다. 다시 시도해주세요.');
      }
    }
  }, [location, navigate, login]);

  // 로딩 중일 때
  if (loading) {
    return (
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundColor: '#f2f2f2'
      }}>
        <div>로딩 중...</div>
      </div>
    );
  }

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
      {/* 로그인 페이지 헤더 */}
      <div style={{ marginBottom: '30px', textAlign: 'center' }}>
        <h1 style={{
          fontSize: '36px',
          color: '#333',
          marginBottom: '10px'
        }}>
          <span style={{ color: '#EA4335' }}>오늘</span>{' '}
          <span style={{ color: '#FBBC05' }}>한잔</span>{' '}
          <span style={{ color: '#4285F4' }}>어때요</span>
          <span style={{ color: '#34A853' }}>?</span>
        </h1>
        <p style={{ color: '#666', fontSize: '18px' }}>
          근처 술집을 찾아서 즐거운 시간을 보내세요
        </p>
      </div>

      {/* 이미지 영역 */}
      <div className="left-div" style={{
        flex: 1,
        padding: '20px',
        backgroundColor: '#f9f9f9',
        textAlign: 'center',
        width: '60%',
        borderRadius: '10px',
        boxShadow: '0 2px 10px rgba(0,0,0,0.1)',
        marginBottom: '30px'
      }}>
        <img
          src="/images/beer-advertising.jpg"
          alt="술집 찾기 이미지"
          style={{
            height: '400px',
            width: '300px',
            maxWidth: '100%',
            borderRadius: '8px'
          }}
        />
      </div>

      {/* 카카오 로그인 버튼 */}
      <div>
        <a
          href={baseUrl + "/api/v1/oauth2/authorization/kakao"}
          role="button"
          id="kakao-login"
          className="me-2"
          style={{
            display: 'inline-block',
            transition: 'transform 0.2s ease'
          }}
          onMouseOver={(e) => e.target.style.transform = 'scale(1.05)'}
          onMouseOut={(e) => e.target.style.transform = 'scale(1)'}
        >
          <img alt="카카오 로그인" src="/images/kakao_login_medium.png"/>
        </a>
      </div>

      {/* 추가 정보 */}
      <div style={{
        marginTop: '20px',
        textAlign: 'center',
        color: '#666',
        fontSize: '14px'
      }}>
        <p>카카오 계정으로 간편하게 로그인하세요</p>
      </div>
    </div>
  );
};

export default Index;