package com.project.reservation.Controller;

import com.project.reservation.Service.UserService;
import com.project.reservation.model.User;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UseController {

    // 로깅을 위한 Logger 설정
    private static final Logger logger = LoggerFactory.getLogger(UseController.class);

    @Autowired // UserService 의존성 주입
    private UserService userService;

    // 회원가입 페이지 이동
    @GetMapping("/Sign")
    public String showSignUpPage(Model model) {
        logger.info("회원가입 페이지로 이동"); // 로거를 통해 이동 시점 기록
        model.addAttribute("user", new User()); // 빈 User 객체를 모델에 추가하여 폼에 전달
        return "Sign/Sign"; // 회원가입 페이지로 이동
    }

    // 회원가입 처리
    @PostMapping("/Sign")
    public String SignUp(@ModelAttribute User user, Model model) {
        logger.info("회원가입 시도: " + user.getUserId()); // 회원가입 시도 시 로깅
        boolean isSuccess = userService.signUp(user); // 회원가입 서비스 호출
        if (isSuccess) {
            model.addAttribute("message", "회원가입이 완료되었습니다.");
            logger.info("회원가입 성공: " + user.getUserId()); // 회원가입 성공 로깅
            return "redirect:/Login"; // 회원가입 후 로그인 페이지로 리다이렉트
        } else {
            model.addAttribute("error", "회원가입에 실패했습니다. 이미 존재하는 아이디일 수 있습니다.");
            logger.error("회원가입 실패: 이미 존재하는 아이디일 수 있음: " + user.getUserId()); // 회원가입 실패 로깅
            return "Sign/Sign"; // 실패 시 회원가입 페이지로 돌아감
        }
    }

    // 로그인 페이지 이동
    @GetMapping("/Login")
    public String loginPage(HttpSession session, Model model) {

        logger.info("로그인 페이지로 이동"); // 로그인 페이지로 이동 시 로깅
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("message", "이미 로그인된 상태입니다.");
            logger.info("이미 로그인된 상태: " + loggedInUser.getUserId()); // 이미 로그인된 상태 로그
        } else {
            model.addAttribute("message", "로그인해주세요.");
            logger.info("로그인 해주세요"); // 로그인 해주세요만 로깅
        }
        return "Login/Login";  // 로그인 페이지로 이동
    }

    // 로그인 처리
    @PostMapping("/Login")
    public String login(@RequestParam("userId") String userId,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {

        logger.info("로그인 시도: " + userId); // 로그인 시도 로그
        User loggedInUser = userService.getLogin(userId, password);
        if (loggedInUser != null) {
            session.setAttribute("loggedInUser", loggedInUser); // 로그인 성공 시 세션에 회원 정보 저장
            session.setAttribute("isLoggedIn", true); // 로그인 상태 세션에 저장
            logger.info("로그인 성공: " + userId); // 로그인 성공 로그
            return "redirect:/home"; // 로그인 후 마이페이지로 리다이렉트
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
            logger.error("로그인 실패: 잘못된 아이디 또는 비밀번호: " + userId); // 로그인 실패 로그
            return "Login/Login"; // 로그인 실패 시 로그인 페이지로 돌아감
        }
    }

    // 마이페이지 정보 조회
    @GetMapping("/MyPage")
    public String myPage(HttpSession session, Model model) {
        logger.info("마이페이지 조회 시도"); // 마이페이지 조회 시 로깅
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser); // 로그인된 사용자 정보를 마이페이지 모델에 추가
            return "Login/MyPage"; // 마이페이지 뷰로 리다이렉트
        } else {
            logger.warn("로그인되지 않은 사용자 마이페이지 접근 시도"); // 로그인되지 않은 사용자가 마이페이지에 접근한 경우
            return "redirect:/Login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
    }

    // 로그아웃 처리
    @GetMapping("/Logout")
    public String logout(HttpSession session) {
        logger.info("로그아웃 시도"); // 로그아웃 시 로깅
        session.invalidate(); // 세션 무효화
        return "redirect:/home"; // 홈페이지로 리다이렉트
    }
}
