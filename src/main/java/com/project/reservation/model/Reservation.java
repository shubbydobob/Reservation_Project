package com.project.reservation.model;

import jakarta.persistence.*;

@Entity // JPA 엔티티. 이 클래스의 객체를 데이터베이스의 테이블과 매핑
@Table(name = "reservation") // 매핑될 데이터 베이스 테이블 'reservation'
public class Reservation {

    @Id // 테이블에서 기본 키 역할.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값은 데이터베이스에서 자동 생성. IDENTITY를 사용하여 자동 증가되도록 사용.
    private Long id; // id는 Long 타입, auto_increment 설정

    @Column(name = "concert_date") // 테이블의 concert_date 컬럼에 매핑.
    private String concertDate;

    @Column(name = "concert_time") // 테이블의 concert_time 컬럼에 매핑.
    private String concertTime;

    @Column(name = "seats") // 테이블의 seats 컬럼에 매핑.
    private String seats; // 좌석 번호를 저장하는 리스트

    @Column(name = "concert_name") // 테이블의 concert_name 컬럼에 매핑.
    private String concertName; // 공연명 추가

    @Column(name = "user_id", nullable = false)
    private String userId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcertDate() {
        return concertDate;
    }

    public void setConcertDate(String concertDate) {
        this.concertDate = concertDate;
    }

    public String getConcertTime() {
        return concertTime;
    }

    public void setConcertTime(String concertTime) {
        this.concertTime = concertTime;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;

    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", concertDate='" + concertDate + '\'' +
                ", concertTime='" + concertTime + '\'' +
                ", seats='" + seats + '\'' +
                ", concertName='" + concertName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}

