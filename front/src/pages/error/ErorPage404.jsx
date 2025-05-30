const ErrorPage404 = () => {

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh',
      flexDirection: 'column'
    }}>
      <h1>404 - 페이지를 찾을 수 없습니다</h1>
      <a href="/" style={{color: '#34A853', textDecoration: 'none'}}>
        홈으로 돌아가기
      </a>
    </div>
  );
};

export default ErrorPage404;