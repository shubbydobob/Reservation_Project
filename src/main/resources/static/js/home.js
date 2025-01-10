// 초기 변수 설정
let currentSlide = 0; // 현재 슬라이드 인덱스
const container = document.querySelector('.concert-container'); // 슬라이더 컨테이너
const slides = Array.from(document.querySelectorAll('.concert-item')); // 슬라이드 목록
const totalSlides = slides.length; // 슬라이드 개수
const itemsPerView = 4; // 한 번에 보이는 슬라이드 개수
const slideWidth = slides[0].offsetWidth; // 슬라이드 하나의 너비
let slideInterval; // 자동 슬라이드 타이머 저장 변수

// 첫 번째와 마지막 슬라이드 복제
const firstClones = slides.slice(0, itemsPerView).map(slide => slide.cloneNode(true));
const lastClones = slides.slice(-itemsPerView).map(slide => slide.cloneNode(true));

// 복제한 슬라이드를 컨테이너에 추가
firstClones.forEach(clone => container.appendChild(clone)); // 맨 끝에 추가
lastClones.forEach(clone => container.insertBefore(clone, slides[0])); // 맨 앞에 추가

// 업데이트된 슬라이드 리스트와 총 개수
const updatedSlides = Array.from(document.querySelectorAll('.concert-item'));
const totalUpdatedSlides = updatedSlides.length;

// 초기 컨테이너 위치 설정
container.style.transform = `translateX(-${itemsPerView * slideWidth}px)`; // 첫 번째 복제 아이템 위치로 이동
currentSlide = itemsPerView; // 현재 슬라이드 인덱스 설정

// 슬라이더 위치 업데이트 함수
function updateSliderPosition() {
    container.style.transition = `transform 0.5s ease-in-out`; // 슬라이드 이동 애니메이션
    container.style.transform = `translateX(-${currentSlide * slideWidth}px)`;
    console.log(`현재 슬라이드: ${currentSlide}, 위치: ${container.style.transform}`);
}

// 무한 슬라이드를 위한 위치 조정
function resetSliderPosition() {
    container.style.transition = 'none'; // 애니메이션 제거
    if (currentSlide === 0) {
        currentSlide = totalSlides; // 맨 끝으로 이동
        container.style.transform = `translateX(-${currentSlide * slideWidth}px)`;
        console.log('슬라이더 맨 끝으로 재설정');
    } else if (currentSlide === totalSlides + itemsPerView) {
        currentSlide = itemsPerView; // 맨 앞으로 이동
        container.style.transform = `translateX(-${currentSlide * slideWidth}px)`;
        console.log('슬라이더 맨 앞으로 재설정');
    }
}

// 다음 슬라이드로 이동
function nextSlide() {
    currentSlide++;
    updateSliderPosition();
    setTimeout(resetSliderPosition, 500); // 슬라이드 이동 후 위치 재설정
}

// 이전 슬라이드로 이동
function prevSlide() {
    currentSlide--;
    updateSliderPosition();
    setTimeout(resetSliderPosition, 500); // 슬라이드 이동 후 위치 재설정
}

// 자동 슬라이드 기능
function startAutoSlide() {
    slideInterval = setInterval(() => {
        nextSlide(); // 3초마다 다음 슬라이드로 이동
        console.log('자동 슬라이드 작동');
    }, 3000);
}

// 자동 슬라이드 멈춤
function stopAutoSlide() {
    clearInterval(slideInterval);
    console.log('자동 슬라이드 멈춤');
}

// 예약 페이지 표시 함수
function showReservation(id) {
  // 모든 예약 섹션 숨기기
  const reservations = document.querySelectorAll('.reservation');
  reservations.forEach(section => {
    section.style.display = 'none';
  });

  // 선택한 예약 섹션 보이기
  const reservation = document.getElementById(`reservation-${id}`);
  if (reservation) {
    reservation.style.display = 'block';
  }

  console.log(`예약 페이지 ${id} 표시됨`);
}

// 예약 페이지 닫기 함수
function hideReservation() {
  const reservations = document.querySelectorAll('.reservation');
  reservations.forEach(section => {
    section.style.display = 'none';
  });

  console.log('예약 페이지 숨김');
}

// 버튼 클릭 이벤트 연결
document.querySelector('.next-button').addEventListener('click', () => {
    stopAutoSlide(); // 버튼 클릭 시 자동 슬라이드 멈춤
    nextSlide();
    startAutoSlide(); // 다시 자동 슬라이드 시작
});

document.querySelector('.prev-button').addEventListener('click', () => {
    stopAutoSlide(); // 버튼 클릭 시 자동 슬라이드 멈춤
    prevSlide();
    startAutoSlide(); // 다시 자동 슬라이드 시작
});

// 초기 자동 슬라이드 시작
startAutoSlide();
console.log('무한 슬라이더 초기화 완료');
