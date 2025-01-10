   var naver_id_login = new naver_id_login("WvFIrG7DSpEo66yj42r8", "http://localhost:8080/home");
    var state = naver_id_login.getUniqState();
    naver_id_login.setButton("white", 2, 40); // 버튼 색상, 크기, 높이 설정
    naver_id_login.setDomain("http://localhost:8080"); // 서비스 도메인 설정
    naver_id_login.setState(state); // 상태 토큰 설정
    naver_id_login.init_naver_id_login();