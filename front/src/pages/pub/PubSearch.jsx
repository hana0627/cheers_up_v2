import React, { useState, useEffect } from 'react';
import axios from "axios";
import { useNavigate } from 'react-router-dom';

const PubSearch = () => {
  const baseUrl = process.env.REACT_APP_API_URL
  const accessToken = localStorage.getItem('accessToken')

  const [address, setAddress] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // Daum 우편번호 서비스 스크립트 로드
    const script = document.createElement('script');
    script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    script.async = true;
    document.head.appendChild(script);

    return () => {
      // 컴포넌트 언마운트 시 스크립트 제거
      document.head.removeChild(script);
    };
  }, []);

  const handleAddress = () => {
    if (window.daum && window.daum.Postcode) {
      new window.daum.Postcode({
        oncomplete: function(data) {
          setAddress(data.address);
        }
      }).open();
    } else {
      console.error('Daum Postcode API가 로드되지 않았습니다.');
    }
  };

  const pubSearch = () => {
    if (address.trim()) {
      axios.get(baseUrl+"/api/v2/search?address="+encodeURIComponent(address), {
        headers: {
          Authorization: accessToken
        }
      })
        .then(response => {
          navigate("/PubList", {
            state: {
              pubs: response.data.result,    // 검색된 술집 목록
              address: address               // 검색한 주소
            }
          })
        });
    } else {
      alert('주소를 선택해주세요.');
    }
  };

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
      marginBottom: '50px'
    },
    input: {
      padding: '10px',
      fontSize: '18px',
      border: 'none',
      borderRadius: '5px',
      boxShadow: '0px 2px 5px rgba(0, 0, 0, 0.3)',
      outline: 'none',
      width: '600px',
      paddingRight: '40px',
      cursor: 'pointer',
      backgroundColor: 'white'
    },
    searchBtn: {
      backgroundColor: '#34A853',
      color: 'white',
      borderRadius: '5px',
      border: 'none',
      fontSize: '20px',
      fontWeight: 'bold',
      padding: '10px 20px',
      transition: 'background-color 0.3s ease-in-out',
      cursor: 'pointer',
      marginLeft: '10px',
      height: '50px'
    },
    highlight1: { color: '#EA4335' },
    highlight2: { color: '#FBBC05' },
    highlight3: { color: '#4285F4' },
    highlight4: { color: '#34A853' }
  };

  return (
    <div style={styles.body}>
      {/* 페이지 제목 */}
      <h1 style={styles.h1}>
        <span style={styles.highlight1}>오늘</span>{' '}
        <span style={styles.highlight2}>한잔</span>{' '}
        <span style={styles.highlight3}>어때요</span>
        <span style={styles.highlight4}>?</span>
      </h1>

      {/* 주소 검색 영역 */}
      <div style={styles.form}>
        <input type="text" id="address_kakao" name="address" placeholder="어디서 한잔 할까요?" value={address}
          onClick={handleAddress} readOnly style={styles.input}
        />

        {/* 검색 버튼 */}
        <button id="search_btn" style={styles.searchBtn} onClick={pubSearch}
          onMouseOver={(e) => e.target.style.backgroundColor = '#4285F4'}
          onMouseOut={(e) => e.target.style.backgroundColor = '#34A853'}
        >
          Search
        </button>
      </div>
    </div>
  );
};

export default PubSearch;