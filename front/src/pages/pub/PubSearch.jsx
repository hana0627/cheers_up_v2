import React, { useState, useEffect } from 'react';
import axios from "axios";
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

/**
 * PubSearch 컴포넌트 - 술집 검색 페이지
 * 주소를 입력받아 근처 술집을 검색
 */
const PubSearch = () => {
  const baseUrl = process.env.REACT_APP_API_URL;
  const { token, logout, user } = useAuth(); // Context에서 토큰과 로그아웃 함수 가져오기
  const [address, setAddress] = useState('');
  const [searching, setSearching] = useState(false);
  const navigate = useNavigate();

  // Daum 우편번호 서비스 로드
  useEffect(() => {
    const script = document.createElement('script');
    script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    script.async = true;
    script.onerror = () => {
      alert('주소 검색 서비스를 불러올 수 없습니다.');
    };
    document.head.appendChild(script);

    return () => {
      // 컴포넌트 언마운트 시 스크립트 제거
      if (document.head.contains(script)) {
        document.head.removeChild(script);
      }
    };
  }, []);

  /**
   * Daum 우편번호 서비스 실행
   */
  const handleAddress = () => {
    if (window.daum && window.daum.Postcode) {
      new window.daum.Postcode({
        oncomplete: function(data) {
          setAddress(data.address);
        }
      }).open();
    } else {
      alert('Daum Postcode API가 로드되지 않았습니다.');
    }
  };

  const pubSearch = async () => {
    if (!address.trim()) {
      alert('주소를 선택해주세요.');
      return;
    }

    setSearching(true);

    try {
      const response = await axios.get(
        `${baseUrl}/api/v2/search?address=${encodeURIComponent(address)}`,
        { headers: {
            Authorization: token,
            'Content-Type': 'application/json'
          },
          timeout: 10000 // 10초 타임아웃
        }
      );

      // 검색 결과를 PubList 페이지로 전달
      navigate("/PubList", {
        state: {
          pubs: response.data.result,
          address: address
        }
      });
    } catch (error) {
      if (error.response) {
        alert(error.response.data.result)
      }
    } finally {
      setSearching(false);
    }
  };

  /**
   * 로그아웃 처리
   */
  const handleLogout = () => {
    if (window.confirm('로그아웃 하시겠습니까?')) {
      logout();
      navigate('/');
    }
  };

  // 스타일 정의
  const styles = {
    body: {
      fontFamily: 'Arial, sans-serif',
      textAlign: 'center',
      backgroundColor: '#f2f2f2',
      margin: 0,
      padding: 0,
      minHeight: '100vh',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      alignItems: 'center'
    },
    header: {
      width: '100%',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      padding: '20px',
      position: 'absolute',
      top: 0,
      left: 0,
      boxSizing: 'border-box'
    },
    userInfo: {
      color: '#666',
      fontSize: '14px'
    },
    logoutBtn: {
      backgroundColor: '#EA4335',
      color: 'white',
      border: 'none',
      borderRadius: '5px',
      padding: '8px 16px',
      cursor: 'pointer',
      fontSize: '14px'
    },
    h1: {
      marginTop: '50px',
      fontSize: '48px',
      color: '#333',
      marginBottom: '50px'
    },
    form: {
      display: 'flex',
      alignItems: 'center',
      position: 'relative',
      marginBottom: '50px',
      gap: '10px'
    },
    input: {
      padding: '15px',
      fontSize: '18px',
      border: 'none',
      borderRadius: '8px',
      boxShadow: '0px 2px 10px rgba(0, 0, 0, 0.1)',
      outline: 'none',
      width: '500px',
      cursor: 'pointer',
      backgroundColor: 'white',
      transition: 'box-shadow 0.3s ease'
    },
    searchBtn: {
      backgroundColor: '#34A853',
      color: 'white',
      borderRadius: '5px',
      border: 'none',
      fontSize: '18px',
      fontWeight: 'bold',
      padding: '15px 25px',
      transition: 'all 0.3s ease',
      cursor: 'pointer',
      height: '55px',
      minWidth: '100px',
      disabled: searching
    },
    highlight1: { color: '#EA4335' },
    highlight2: { color: '#FBBC05' },
    highlight3: { color: '#4285F4' },
    highlight4: { color: '#34A853' }
  };

  return (
    <div style={styles.body}>
      {/* 헤더 - 사용자 정보 및 로그아웃 */}
      <div style={styles.header}>
        <div style={styles.userInfo}>
          {user ? `안녕하세요, ${user.nickname || '사용자'}님` : '환영합니다'}
        </div>
        <button style={styles.logoutBtn} onClick={handleLogout}
          onMouseOver={(e) => e.target.style.backgroundColor = '#d33'}
          onMouseOut={(e) => e.target.style.backgroundColor = '#EA4335'}
        >
          로그아웃
        </button>
      </div>

      {/* 메인 콘텐츠 */}
      <div style={{ marginTop: '80px' }}>
        {/* 페이지 제목 */}
        <h1 style={styles.h1}>
          <span style={styles.highlight1}>오늘</span>{' '}
          <span style={styles.highlight2}>한잔</span>{' '}
          <span style={styles.highlight3}>어때요</span>
          <span style={styles.highlight4}>?</span>
        </h1>

        {/* 주소 검색 영역 */}
        <div style={styles.form}>
          <input type="text" id="address_kakao" name="address" placeholder="어디서 한잔 할까요? (클릭하여 주소 검색)" value={address}
            onClick={handleAddress} readOnly
            style={{
              ...styles.input,
              ':hover': {
                boxShadow: '0px 4px 15px rgba(0, 0, 0, 0.15)'
              }
            }}
          />

          {/* 검색 버튼 */}
          <button id="search_btn" style={styles.searchBtn} onClick={pubSearch}
            onMouseOver={(e) => !searching && (e.target.style.backgroundColor = '#4285F4')}
            onMouseOut={(e) => !searching && (e.target.style.backgroundColor = '#34A853')}
            disabled={searching}
          >
            {searching ? '검색 중...' : 'Search'}
          </button>
        </div>

        {/* 도움말 텍스트 */}
        <div style={{
          color: '#666',
          fontSize: '16px',
          marginTop: '30px',
          maxWidth: '600px'
        }}>
          <p>📍 주소를 입력하면 근처 술집을 찾아드립니다</p>
          <p>🍺 맛있는 음식과 즐거운 시간을 보내세요</p>
        </div>
      </div>
    </div>
  );
};

export default PubSearch;