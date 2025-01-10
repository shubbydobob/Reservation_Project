package com.project.reservation.Controller;

import com.project.reservation.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {

    // 로거 인스턴스 생성
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    // home 페이지로 이동
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // 로그인한 유저가 존재하면
        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser); // 로그인한 유저를 모델에 추가
            model.addAttribute("isLoggedIn", true); // 로그인 상태 true 설정
            logger.info("세션에 저장된 사용자: " + loggedInUser); // 로그에 세션에 저장된 사용자 정보 출력
            logger.info("세션에 저장된 사용자 이름: " + loggedInUser.getName()); // 로그에 사용자 이름 출력
        } else {
            model.addAttribute("isLoggedIn", false); // 로그인 상태 false 설정
            logger.info("세션에 저장된 사용자 없음"); // 로그에 세션에 저장된 사용자 없음 출력
        }
        // home 뷰 반환
        return "home"; // Return the home view
    }
}