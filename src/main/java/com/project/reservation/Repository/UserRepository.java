package com.project.reservation.Repository;

import com.project.reservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    // userId로 사용자 조회
    User findByUserId(String userId);
}
