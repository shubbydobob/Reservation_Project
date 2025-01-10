package com.project.reservation.model;

import jakarta.persistence.*;

@Entity // JPA 엔티티. 이 클래스의 객체를 데이터베이스의 테이블과 매핑
@Table(name = "user") // 매핑될 데이터 베이스 테이블 'user'
public class User {
    @Id // 테이블에서 기본 키 역할.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값은 데이터베이스에서 자동 생성. IDENTITY를 사용하여 자동 증가되도록 사용.
    @Column(name = "id") // 테이블의 id 컬럼에 매핑
    private Long id; // id는 Long 타입, auto_increment 설정

    @Column(name = "user_id", nullable = false, unique = true) // 테이블의 user_id 컬럼에 매핑. Null 값이 될 수 없음.
    private String userId; // userId는 기본키로 설정

    @Column(name = "password", nullable = false) // 테이블의 password 컬럼에 매핑. Null 값이 될 수 없음.
    private String password;

    @Column(name = "name", nullable = false) // 테이블의 name 컬럼에 매핑. Null 값이 될 수 없음.
    private String name;

    @Column(name = "gender", nullable = false) // 테이블의 gender 컬럼에 매핑. Null 값이 될 수 없음.
    private String gender;

    @Column(name = "phone", nullable = false) // 테이블의 phone 컬럼에 매핑. Null 값이 될 수 없음.
    private String phone;

    @Column(name = "email", nullable = false) // 테이블의 email 컬럼에 매핑. Null 값이 될 수 없음.
    private String email;

    // getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
