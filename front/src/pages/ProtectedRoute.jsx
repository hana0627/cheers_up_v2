// 인증이 필요한 페이지를 보호하는 컴포넌트
import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from './context/AuthContext';

/**
 * ProtectedRoute 컴포넌트
 * 인증된 사용자만 접근할 수 있는 라우트를 보호
 * @param {React.ReactNode} children - 보호할 컴포넌트
 * @param {string} requiredRole - 필요한 권한 (선택사항)
 */
const ProtectedRoute = ({ children, requiredRole = null }) => {
  const { isAuthenticated, loading, user } = useAuth();
  const location = useLocation();

  // 로딩 중일 때 로딩 스피너 표시
  if (loading) {
    return (
      <div style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundColor: '#f2f2f2',
        flexDirection: 'column'
      }}>
        <div style={{
          border: '4px solid #f3f3f3',
          borderTop: '4px solid #34A853',
          borderRadius: '50%',
          width: '40px',
          height: '40px',
          animation: 'spin 1s linear infinite'
        }}></div>
        <p style={{ marginTop: '20px', fontSize: '18px', color: '#666' }}>
          인증 확인 중...
        </p>
        <style jsx>{`
          @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
          }
        `}</style>
      </div>
    );
  }

  // 인증되지 않은 경우 로그인 페이지로 리다이렉트
  if (!isAuthenticated) {
    return <Navigate to="/" state={{ from: location }} replace />;
  }
  // 모든 조건을 통과하면 자식 컴포넌트 렌더링
  return children;
};

export default ProtectedRoute;