package com.project.reservation.Controller;

import com.project.reservation.Service.ReservationService;
import com.project.reservation.model.Reservation;
import com.project.reservation.model.User;
import com.project.reservation.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.project.reservation.Repository.ReservationRepository;

import java.util.List;

@Controller
@RequestMapping("/Reservation")
public class ReservationController {

    // Logger 생성
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired // ReservationService 의존성 주입
    private ReservationService reservationService;

    @Autowired // UserService 의존성 주입
    private UserService userService;

    @Autowired // ReservationRepository 의존성 주입
    private ReservationRepository reservationRepository;

    // 예약 페이지 이동
    @GetMapping
    public String getReservationsByUserName(Model model, HttpSession session) {
        // 세션에서 로그인한 사용자 정보를 가져오기.
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // 사용자가 로그인한 상태인지 확인
        if (loggedInUser != null) {
            String userId = loggedInUser.getUserId();  // 로그인한 사용자의 아이디 가져오기
            // 사용자 아이디로 예약 목록을 조회
            List<Reservation> reservations = reservationService.getReservationsByUserId(userId);  // 예약 목록 조회

            model.addAttribute("loggedInUser", loggedInUser); // 사용자 정보도 전달
            model.addAttribute("reservations", reservations); // 조회된 예약 목록을 모델에 추가하여 뷰로 전달
            logger.info("User '{}' reservations fetched successfully", userId); // 예약 목록 조회 성공 로그
        } else {
            // 사용자가 로그인하지 않았다면, 로그인 메시지를 추가
            model.addAttribute("message", "로그인이 필요합니다.");
            logger.warn("사용자가 로그인하지 않았습니다. 로그인 페이지로 리다이렉트됩니다."); // 로그인하지 않은 상태 로그
            return "redirect:/Login"; // 로그인하지 않은 경우 로그인 페이지로 이동
        }

        return "Reservation/Reservation"; // 예약 페이지 이동
    }

    // 예약 폼 처리
    @PostMapping
    public String submitReservation(@ModelAttribute Reservation reservation,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {

        // 세션에서 로그인된 사용자 정보 가져오기
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // 로그인 여부 확인
        if (loggedInUser == null) {
            logger.warn("User is not logged in, redirecting to login page");
            return "redirect:/Login"; // 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
        }

        // 예약 객체와 로그인 사용자 정보 출력
        logger.info("Received reservation: {}", reservation); // 예약 정보
        logger.info("예약하는 사용자: ID = {}, 이름 = {}", loggedInUser.getUserId(), loggedInUser.getName());

        // 좌석 중복 여부 확인
        if (!reservationService.isSeatAvailable(reservation.getConcertDate(), reservation.getConcertTime(), reservation.getSeats())) {
            logger.warn("선택된 좌석 '{}'은 이미 예약되었습니다. 날짜: '{}', 시간: '{}'", reservation.getSeats(), reservation.getConcertDate(), reservation.getConcertTime());
            redirectAttributes.addFlashAttribute("error", "선택한 좌석은 이미 예약되었습니다.");
            return "redirect:/Reservation"; // 중복 좌석 에러 발생 시 예약 페이지로 리다이렉트
        }

        // 예약 객체에 사용자 ID 설정
        reservation.setUserId(loggedInUser.getUserId());
        reservationService.saveReservation(reservation); // 예약 저장

        // 세션에 예약 정보 저장
        session.setAttribute("reservation", reservation); // 예약 정보를 세션에 저장

        // 리다이렉트 시 모델에 예약 정보와 사용자 정보 추가
        redirectAttributes.addFlashAttribute("reservation", reservation);
        redirectAttributes.addFlashAttribute("loggedInUser", loggedInUser);

        logger.info("Reservation for user '{}' saved successfully", loggedInUser.getName()); // 예약 저장 성공 로그
        return "redirect:/Reservation/Reservation-Confirm"; // 예약 확인 페이지로 리다이렉트
    }

    @GetMapping("Reservation-Confirm")
    public String getConfirmationPage(HttpSession session, Model model) {

        // 세션에서 예약 정보와 로그인된 사용자 정보 가져오기
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Reservation reservation = (Reservation) session.getAttribute("reservation");

        if (reservation == null && loggedInUser != null) {
            // 세션에서 예약 정보가 없으면 DB에서 해당 userId로 예약 정보를 조회
            reservation = reservationService.getReservationByUserId(loggedInUser.getUserId());
        }

        // 로그로 로그인된 사용자와 예약 정보를 출력하여 디버깅하기
        logger.info("LoggedInUser: {}", loggedInUser);
        logger.info("Reservation: {}", reservation);

        // 예약 정보가 있을 경우 모델에 추가하여 예약 정보를 확인 페이지에 표시
        if (reservation != null) {
            model.addAttribute("reservation", reservation);
            logger.info("예약 확인 페이지에 예약 id: {}를 표시", reservation.getId());
        } else {
            logger.warn("세션에서 예약 정보를 찾을 수 없음.");
        }

        // 로그인된 사용자 정보가 있을 경우 모델에 추가하여 사용자 정보를 확인 페이지에 표시
        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
            logger.info("예약 확인 페이지에 로그인한 사용자: {} 표시", loggedInUser.getName());
        } else {
            // 로그인된 사용자 정보가 세션에 없을 경우 경고 로그 출력
            logger.warn("세션에서 로그인된 사용자 정보를 찾을 수 없음.");
        }
        return "Reservation/Reservation-Confirm"; // 예약 확인 페이지 반환
    }

    // 예약 취소 페이지로 이동하는 메소드
    @GetMapping("/Reservation-Cancel")
    public String cancelReservation(HttpSession session, Model model) {
        // 세션에서 예약 정보와 로그인된 사용자 정보 가져오기
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Reservation reservation = (Reservation) session.getAttribute("reservation");

        // 예약이 없거나 사용자가 로그인되지 않은 경우 처리
        if (reservation == null || loggedInUser == null) {
            model.addAttribute("message", "예약 취소를 위한 유효한 정보가 없습니다.");
            return "Reservation/Reservation-Cancel";  // 취소 페이지로 이동
        }

        // 예약 취소 로직
        reservationService.deleteReservation(reservation);  // 서비스에서 예약 삭제 메소드 호출

        // 세션에서 예약 정보 제거
        session.removeAttribute("reservation");

        // 취소된 예약 정보를 모델에 전달
        model.addAttribute("cancelledReservation", reservation);
        model.addAttribute("loggedInUser", loggedInUser);

        logger.info("예약 취소가 완료되었습니다. 취소된 예약 ID: {}", reservation.getId());

        return "Reservation/Reservation-Cancel";  // 취소된 예약 내역을 보여줄 페이지
    }
}
