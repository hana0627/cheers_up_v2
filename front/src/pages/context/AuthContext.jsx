import React, { createContext, useContext, useState, useEffect } from 'react';

// Context 생성
const AuthContext = createContext();

// Context 사용을 위한 커스텀 훅
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth는 AuthProvider 내부에서만 사용할 수 있습니다.');
  }
  return context;
};

// AuthProvider 컴포넌트 - 앱 전체를 감싸서 인증 상태를 제공
export const AuthProvider = ({ children }) => {
  // 인증 관련 상태들
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true); // 초기 로딩 상태
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null); // 사용자 정보 (필요시)

  /**
   * JWT 토큰 유효성 검사 함수
   * @param {string} token - 검사할 JWT 토큰
   * @returns {boolean} - 토큰이 유효한지 여부
   */
  const isValidToken = (token) => {
    if (!token) {
      // 토큰이 없는 경우
      return false;
    }

    try {
      // JWT는 header.payload.signature 형태로 구성
      const parts = token.split('.');
      if (parts.length !== 3) {
        return false;
      }

      // payload 부분을 디코딩하여 만료시간 확인
      const payload = JSON.parse(atob(parts[1]));
      setUser({
        nickname: decodeURIComponent(escape(payload.nickname)) || '',
        roleType: payload.roleType || 'USER',
        userId: decodeURIComponent(escape(payload.userId)) || '',
        email: decodeURIComponent(escape(payload.email)) || '',
        iat: payload.iat,
        exp: payload.exp
      });
      const currentTime = Math.floor(Date.now() / 1000); // 현재 시간을 초 단위로

      // exp(만료시간)이 현재시간보다 큰지 확인
      if (payload.exp && payload.exp > currentTime) {
        // 유효한 토큰
        return true;
      } else {
        // 만료된 토큰
        return false;
      }
    } catch (error) {
      // 토큰 검증중 에러 발생
      return false;
    }
  };

  /**
   * 로그인 함수
   * @param {string} accessToken - 서버에서 받은 JWT 토큰
   * @returns {boolean} - 로그인 성공 여부
   */
  const login = (accessToken, userData = null) => {
    if (isValidToken(accessToken)) {
      // 로컬 스토리지에 토큰 저장
      localStorage.setItem('accessToken', accessToken);
      // 상태 업데이트
      setToken(accessToken);
      setIsAuthenticated(true);
      setUser(userData);
      return true;
    } else {
      return false;
    }
  };

  /**
   * 로그아웃 함수
   */
  const logout = () => {
    // 로컬 스토리지에서 토큰 제거
    localStorage.removeItem('accessToken');

    // 상태 초기화
    setToken(null);
    setIsAuthenticated(false);
    setUser(null);
  };

  /**
   * 토큰 갱신 함수 (필요시 서버 API 호출)
   * @returns {Promise<boolean>} - 갱신 성공 여부
   */
  const refreshToken = async () => {
    try {
      // TODO 토큰 갱신
      return false;
    } catch (error) {
      // 갱신실패
      logout();
      return false;
    }
  };

  // 컴포넌트 마운트 시 기존 토큰 확인
  useEffect(() => {
    const checkExistingToken = async () => {
      const storedToken = localStorage.getItem('accessToken');

      if (storedToken) {
        if (isValidToken(storedToken)) {
          // 유효한 토큰이 있으면 로그인 상태로 설정
          setToken(storedToken);
          setIsAuthenticated(true);
        } else {
          // 무효한 토큰이면 제거
        }
      }
      // 로딩 완료
      setLoading(false);
    };

    checkExistingToken();
  }, []);

  // Context에서 제공할 값들
  const contextValue = {
    // 상태
    isAuthenticated,
    token,
    user,
    loading,

    // 함수들
    login,
    logout,
    refreshToken,
    isValidToken
  };

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  );
};
