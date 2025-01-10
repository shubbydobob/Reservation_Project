package com.project.reservation.Repository;

import com.project.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 추가적인 예약 관련 쿼리를 작성할 수 있습니다.
    // 사용자 이름으로 예약을 조회하는 메서드
    List<Reservation> findByUserId(String userId);

    // 동일 날짜, 시간, 좌석으로 예약된 건수 확인
    int countByConcertDateAndConcertTimeAndSeats(String concertDate, String concertTime, String seats);

    // 특정 userId에 대한 예약 정보를 최신순으로 조회 (한 건만 반환)
    Reservation findTopByUserIdOrderByConcertDateDesc(String userId);
}
