package com.store.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * 계정
     */
    ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디나 패스워드를 확인해주세요."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 회원정보가 없습니다."),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "유효하지않은 접근입니다."),

    /**
     * 매장
     */
    NORMAL_CLASS(HttpStatus.BAD_REQUEST, "일반회원 입니다."),
    SAME_RESTAURANT_NAME(HttpStatus.BAD_REQUEST, "상호명이 같은 매장이 있습니다."),
    NO_RESTAURANT_REGISTERED(HttpStatus.BAD_REQUEST, "등록된 매장이 없습니다."),

    /**
     * 예약
     */
    MEMBER_CLASS(HttpStatus.BAD_REQUEST, "매니저 입니다."),
    NO_CORRESPONDING_DATA_RESERVATION_NUMBER(HttpStatus.BAD_REQUEST, "예약번호에 해당하는 데이터가 없습니다."),

    /**
     * 키오스크 (예약 확인)
     */
    CONFIRM_RESERVATION_NUMBER_AND_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "예약번호와 전화번호를 확인해주세요."),
    RESERVATION_NUMBER_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 사용한 예약번호입니다."),
    RESERVATION_CANCELLED(HttpStatus.BAD_REQUEST, "예약취소된 예약번호입니다."),
    ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 사용된 예약번호입니다."),
    NOT_APPROVED(HttpStatus.BAD_REQUEST, "승인이 안된 예약번호입니다."),
    RESERVATION_TIME_EXCEEDED(HttpStatus.BAD_REQUEST, "예약시간이 넘었습니다."),
    CHECK_IT_10_MINUTES_BEFORE_THE_RESERVATION_TIME(HttpStatus.BAD_REQUEST,
        "예약시간 10분 전부터 확인 가능합니다."),

    /**
     * 리뷰
     */
    NOT_A_USE_RESERVATION_NUMBER(HttpStatus.BAD_REQUEST, "사용한 예약번호가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
