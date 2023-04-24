package com.store.reservation.domain.reservation.model;

import com.store.reservation.domain.reservation.common.ApprovalType;
import com.store.reservation.domain.reservation.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Long reservationId;
    private String reservationNumber;
    private String userName;
    private LocalDate reservationDate;
    private LocalDateTime reservationDateTime;
    private String userPhoneNumber;
    private ApprovalType approval;


    /**
     * restaurant -> ReservationDto
     *
     * @param reservation reservation 객체
     * @return ReservationDto
     */
    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(reservation.getReservationId(),
            reservation.getReservationNumber(),
            reservation.getUserName(), reservation.getReservationDate(),
            reservation.getReservationDateTime(), reservation.getPhoneNumber(),
            reservation.getApproval());
    }
}
