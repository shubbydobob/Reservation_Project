package com.project.reservation.Service;

import com.project.reservation.Repository.ReservationRepository;
import com.project.reservation.model.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    // 로그 기록을 위한 Logger 객체 생성
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired // UserRepository 의존성 주입
    private ReservationRepository reservationRepository;

    // 예약 정보 저장
    public void saveReservation(Reservation reservation) {
        try {
            // 예약 정보를 데이터베이스에 저장
            logger.info("Attempting to save reservation for concert: {}", reservation.getConcertName());
            reservationRepository.save(reservation);
            logger.info("Reservation saved successfully for concert: {}", reservation.getConcertName());
        } catch (Exception e) {
            // 예외가 발생한 경우 에러 로그 출력
            logger.error("Error saving reservation for concert: {}", reservation.getConcertName(), e);
        }
    }

    // 사용자 이름으로 예약 목록 조회
    public List<Reservation> getReservationsByUserId(String userId) {
        // 예약 목록 조회 시 로그 추가
        logger.info("Fetching reservations for user: {}", userId);
        List<Reservation> reservations = reservationRepository.findByUserId(userId);

        // 예약 목록이 없는 경우
        if (reservations.isEmpty()) {
            logger.warn("No reservations found for user: {}", userId);
        } else {
            logger.info("Found {} reservations for user: {}", reservations.size(), userId);
        }

        return reservations;
    }

    // 특정 userId에 대한 예약을 조회하는 메서드
    public Reservation getReservationByUserId(String userId) {
        // 예약 테이블에서 해당 userId에 대한 예약 정보를 조회
        return reservationRepository.findTopByUserIdOrderByConcertDateDesc(userId); // 예시로 가장 최근 예약을 가져오는 쿼리
    }

    // 예약 가능한 좌석 확인
    public boolean isSeatAvailable(String concertDate, String concertTime, String seats) {
        logger.info("좌석 중복 여부 확인: 날짜 '{}', 시간 '{}', 좌석 '{}'", concertDate, concertTime, seats);

        // 동일 날짜, 시간, 좌석으로 예약된 건수 확인
        boolean isAvailable = reservationRepository.countByConcertDateAndConcertTimeAndSeats(concertDate, concertTime, seats) == 0;

        if (isAvailable) {
            logger.info("좌석 '{}'은 예약 가능합니다.", seats);
        } else {
            logger.warn("좌석 '{}'은 이미 예약되었습니다.", seats);
        }

        return isAvailable;
    }

    // 예약 취소
    public void deleteReservation(Reservation reservation) {
        if (reservation != null) {
            reservationRepository.delete(reservation);  // 해당 예약 삭제
            logger.info("Reservation with ID '{}' has been deleted", reservation.getId());
        }
    }
}