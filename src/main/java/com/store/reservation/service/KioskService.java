package com.store.reservation.service;

import static com.store.reservation.exception.ErrorCode.ALREADY_USED;
import static com.store.reservation.exception.ErrorCode.CHECK_IT_10_MINUTES_BEFORE_THE_RESERVATION_TIME;
import static com.store.reservation.exception.ErrorCode.CONFIRM_RESERVATION_NUMBER_AND_PHONE_NUMBER;
import static com.store.reservation.exception.ErrorCode.NOT_APPROVED;
import static com.store.reservation.exception.ErrorCode.RESERVATION_CANCELLED;
import static com.store.reservation.exception.ErrorCode.RESERVATION_TIME_EXCEEDED;

import com.store.reservation.domain.reservation.common.ApprovalType;
import com.store.reservation.domain.reservation.entity.Reservation;
import com.store.reservation.domain.reservation.model.CheckReservationForm;
import com.store.reservation.exception.CustomException;
import com.store.reservation.repository.ReservationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KioskService {

    private final ReservationRepository reservationRepository;

    /**
     * 예약 번호 확인
     *
     * @param restaurantid         매장 아이디
     * @param checkReservationForm 예약번호 확인 폼
     * @return String
     */
    @Transactional
    public String checkReservation(Long restaurantid, CheckReservationForm checkReservationForm) {
        String reservationNumber = checkReservationForm.getReservationNumber();
        String phoneNumber = checkReservationForm.getPhoneNumber();

        // 예약 확인
        Reservation reservation =
            reservationRepository.findReservation(restaurantid, reservationNumber, phoneNumber)
                .orElseThrow(
                    () -> new CustomException(CONFIRM_RESERVATION_NUMBER_AND_PHONE_NUMBER));

        // 승인 여부 확인
        ApprovalType approvalType = reservation.getApproval();
        // 취소
        if (approvalType.equals(ApprovalType.CANCEL)) {
            throw new CustomException(RESERVATION_CANCELLED);

            // 사용
        } else if (approvalType.equals(ApprovalType.USED)) {
            throw new CustomException(ALREADY_USED);

            // 대기
        } else if (approvalType.equals(ApprovalType.STANDBY)) {
            throw new CustomException(NOT_APPROVED);
        }

        // 10분 이내 확인
        LocalDateTime time = reservation.getReservationDateTime();
        // 예약시간이 지났을 경우
        if (!time.isAfter(LocalDateTime.now())) {
            throw new CustomException(RESERVATION_TIME_EXCEEDED);

            // 예약시간 10분전부터
        } else if (LocalDateTime.now().isBefore(time.minusMinutes(10))) {
            throw new CustomException(CHECK_IT_10_MINUTES_BEFORE_THE_RESERVATION_TIME);
        }

        // 예약번호 상태 변경 (승인 -> 사용)
        reservation.setApproval(ApprovalType.USED);
        reservationRepository.save(reservation);

        String name = reservation.getUserName();

        return name + "님 예약번호 " + reservationNumber + " 확인되었습니다.";
    }
}
