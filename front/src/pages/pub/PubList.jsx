import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';

const PubList = () => {
  const accessToken = localStorage.getItem('accessToken');
  const location = useLocation();
  const navigate = useNavigate();
  const baseUrl = process.env.REACT_APP_API_URL;

  const [pubs, setPubs] = useState([]);
  const [address, setAddress] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // Daum 우편번호 서비스 스크립트 로드
    const script = document.createElement('script');
    script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    script.async = true;
    document.head.appendChild(script);

    // 전달받은 데이터가 있다면 사용
    if (location.state && location.state.pubs) {
      setPubs(location.state.pubs);
      setAddress(location.state.address || '');
    }

    return () => {
      // 컴포넌트 언마운트 시 스크립트 제거
      if (document.head.contains(script)) {
        document.head.removeChild(script);
      }
    };
  }, [location.state]);

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

  const pubSearch = () => {
    if (address.trim()) {
      setLoading(true);

      axios.get(`${baseUrl}/api/v2/search?address=${encodeURIComponent(address)}`, {
        headers: {
          // 인증을 위한 JWT 토큰을 헤더에 포함
          Authorization: accessToken
        }
      })
        .then(response => {
          if (response.data && response.data.result) {
            setPubs(response.data.result);
          }
          setLoading(false);
        })

        .catch(error => {
          if (error.response && error.response.data && error.response.data.result) {
            alert(error.response.data.result);
            navigate("/")
          }
          setLoading(false);
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
      marginTop: '50px',
      display: 'inline-block',
      position: 'relative',
      marginBottom: '30px'
    },
    inputContainer: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      gap: '10px'
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
      height: '50px'
    },
    container: {
      width: '80%',
      margin: '0 auto',
      fontFamily: 'Arial, Helvetica, sans-serif'
    },
    pub: {
      border: '1px solid #ccc',
      padding: '10px',
      marginBottom: '20px',
      backgroundColor: 'white',
      borderRadius: '5px',
      textAlign: 'left'
    },
    pubTitle: {
      fontSize: '1.5rem',
      marginBottom: '5px',
      color: '#333'
    },
    address: {
      fontSize: '1rem',
      marginBottom: '10px',
      color: '#666'
    },
    distance: {
      fontSize: '0.8rem',
      marginBottom: '10px',
      color: '#666'
    },
    category: {
      fontSize: '0.9rem',
      marginBottom: '10px',
      color: '#888'
    },
    links: {
      display: 'flex',
      justifyContent: 'space-between',
      marginTop: '10px',
      gap: '10px'
    },
    linkButton: {
      backgroundColor: '#4CAF50',
      color: 'white',
      padding: '8px 16px',
      textAlign: 'center',
      textDecoration: 'none',
      display: 'inline-block',
      fontSize: '0.8rem',
      borderRadius: '5px',
      cursor: 'pointer',
      border: 'none',
      transition: 'background-color 0.3s ease'
    },
    highlight1: { color: '#EA4335' },
    highlight2: { color: '#FBBC05' },
    highlight3: { color: '#4285F4' },
    highlight4: { color: '#34A853' },
    loading: {
      fontSize: '18px',
      color: '#666',
      margin: '20px'
    },
    hr: {
      margin: '30px 0',
      border: 'none',
      borderTop: '1px solid #ccc'
    }
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

      {/* 재검색을 위한 주소 검색 영역 */}
      <div style={styles.form}>
        <div style={styles.inputContainer}>
          {/* 주소 입력창 */}
          <input type="text" id="address_kakao" name="address" placeholder="어디서 한잔 할까요?" value={address}
            onClick={handleAddress} readOnly style={styles.input}
          />
          {/* 검색 버튼 */}
          <button id="search_btn" style={styles.searchBtn} onClick={pubSearch}
            // 마우스 호버 효과
            onMouseOver={(e) => e.target.style.backgroundColor = '#4285F4'}
            onMouseOut={(e) => e.target.style.backgroundColor = '#34A853'}
            disabled={loading}
          >
            {loading ? 'Loading...' : 'Search'}
          </button>
        </div>
      </div>

      {/* 구분선 */}
      <hr style={styles.hr} />

      {/* 검색 결과 표시 영역 */}
      <div style={styles.container}>
        {loading && <div style={styles.loading}>검색 중...</div>}
        {!loading && pubs.length === 0 && (
          <div style={styles.loading}>검색 결과가 없습니다.</div>
        )}

        {!loading && pubs.map((pub, index) => (
          <div key={index} style={styles.pub}>

            <h2 style={styles.pubTitle}>{pub.pubName}</h2>{/* 술집 이름 */}
            <div style={styles.address}>{pub.pubAddress}</div>{/* 술집 주소 */}
            <div style={styles.distance}>{pub.distance}</div>{/* 거리 정보 */}
            {pub.categoryName && (
              <div style={styles.category}>{pub.categoryName}</div>
            )}{/* 카테고리 정보 (있을 경우에만 표시) */}

            {/* 길안내, 로드뷰 링크 버튼들 */}
            <div style={styles.links}>
              {/* 길안내 버튼 (카카오맵으로 연결) */}
              <a href={pub.directionUrl} target="_blank" rel="noopener noreferrer" style={styles.linkButton}
                // 마우스 호버 효과
                onMouseOver={(e) => e.target.style.backgroundColor = '#3e8e41'}
                onMouseOut={(e) => e.target.style.backgroundColor = '#4CAF50'}
              >
                길안내
              </a>

              {/* 로드뷰 버튼 (카카오맵 로드뷰로 연결) */}
              <a href={pub.roadViewUrl} target="_blank" rel="noopener noreferrer" style={styles.linkButton}
                // 마우스 호버 효과
                onMouseOver={(e) => e.target.style.backgroundColor = '#3e8e41'}
                onMouseOut={(e) => e.target.style.backgroundColor = '#4CAF50'}
              >
                로드뷰
              </a>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default PubList;
