package com.project.reservation.Service;

import com.project.reservation.Repository.UserRepository;
import com.project.reservation.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 이 클래스는 비즈니스 로직을 처리하는 서비스 클래스임을 나타냄
@Transactional // 해당 클래스의 모든 메서드는 트랜잭션을 지원
public class UserService {

    // 로거 객체 생성: UserService 클래스에서 발생하는 로그를 기록
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired // UserRepository 의존성 주입
    private UserRepository userRepository;

    // 회원가입 처리
    // user 객체를 받아 데이터베이스에 저장하고 성공 여부를 반환
    public boolean signUp(User user) {
        try {
            // 회원가입 시도 로그 (로그인 시도하는 사용자 ID를 기록)
            logger.info("Attempting to save user with ID: {}", user.getUserId());

            // User 객체를 데이터베이스에 저장
            User savedUser = userRepository.save(user);
            // 성공적으로 저장되었을 때 로그
            logger.info("User saved successfully: {}", savedUser.getUserId());
            return true;
        } catch (Exception e) {
            // 예외 발생 시 오류 로그
            logger.error("Error occurred while saving user with ID: {}", user.getUserId(), e);
            return false;
        }
    }

    // 로그인 처리 (userId와 password로 인증)
    public User getLogin(String userId, String password) {

        // 로그인 시도 로그
        logger.info("Attempting to log in with userId: {}", userId);

        // User 객체에서 userId를 사용하여 데이터베이스에서 해당 사용자 정보를 조회
        User user = userRepository.findByUserId(userId);
        if (user != null && user.getPassword().equals(password)) {
            // 로그인 성공 로그
            logger.info("Login successful for userId: {}", userId);
            return user; // 인증 성공
        }
        // 로그인 실패 시 경고 로그
        logger.warn("Login failed for userId: {}", userId);
        return null; // 인증 실패
    }

    // 현재 로그인된 사용자 가져오기 (세션에서 찾기)
    public User getLoggedInUser(String userId) {
        // 로그인된 사용자 정보 조회 로그
        logger.info("Fetching logged-in user with userId: {}", userId);
        return userRepository.findByUserId(userId);
    }

    // 회원 정보 조회
    public User getMyPage(String userId) {
        // 회원 정보 조회 로그
        logger.info("Fetching user with userId: {}", userId);
        return userRepository.findByUserId(userId);
    }

    // 회원 정보 업데이트
    public boolean updateUser(User user) {
        // 회원이 존재하는지 확인하는 로그
        if (userRepository.existsById(user.getUserId())) {
            // 사용자 정보 업데이트
            userRepository.save(user);
            // 업데이트 성공 로그
            logger.info("User information updated successfully for userId: {}", user.getUserId());
            return true;
        }
        // 사용자 ID가 존재하지 않을 경우 경고 로그
        logger.warn("UserId not found for update: {}", user.getUserId());
        return false; // userId가 존재하지 않으면 false 반환
    }
}
