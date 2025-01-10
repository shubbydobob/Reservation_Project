// 회원가입 폼 제출 이벤트 처리
document.getElementById('signupForm').addEventListener('submit', function (event) {
    const userId = document.getElementById('userId').value;
    const password = document.getElementById('password').value;
    const phone = document.getElementById('phone').value;
     const formData = new FormData(this);

    // 간단한 유효성 체크
    if (userId.length < 6 || userId.length > 12) {
        alert('아이디는 6~12자 사이여야 합니다.');
        event.preventDefault();
        return;
    }

    if (password.length < 8) {
        alert('비밀번호는 최소 8자 이상이어야 합니다.');
        event.preventDefault();
        return;
    }

    
    // 서버에 POST 요청
    fetch('/Sign', {
        method: 'POST',
        body: formData,
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 회원가입 성공 시
                alert('회원가입되었습니다.');
                window.location.href = '/Login'; // 홈 페이지로 이동
            } else {
                // 실패 시
                alert(`회원가입에 실패했습니다: ${data.message}`);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('회원가입 중 오류가 발생했습니다.');
});
});

// 아이디 중복 체크 버튼 클릭 이벤트 처리
document.getElementById('checkId').addEventListener('click', function () {
    const userId = document.getElementById('userId').value;
    if (!userId) {
        alert('아이디를 입력해주세요.');
        return;
    }

    // 서버와 통신하여 아이디 중복 체크
    fetch(`/check-id?userId=${userId}`)
        .then(response => response.json())
        .then(data => {
            if (data.available) {
                alert('사용 가능한 아이디입니다.');
            } else {
                alert('이미 사용 중인 아이디입니다.');
            }
        })
        .catch(error => console.error('Error:', error));
});