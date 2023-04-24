package com.store.reservation.domain.reservation.model;

import com.store.reservation.domain.reservation.common.ApprovalType;
import com.store.reservation.domain.reservation.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResRestaurantList {

    private Long restaurantId;
    private String restaurantName;

    private List<ReservationList> reservationList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationList {

        private Long reservationId;
        private String reservationNumber;
        private String userName;
        private LocalDate reservationDate;
        private LocalDateTime reservationDateTime;
        private String userPhoneNumber;
        private ApprovalType approval;


        /**
         * Reservation -> ReservationList
         *
         * @param reservation reservation 객체
         * @return ReservationList
         */
        public static ReservationList fromList(Reservation reservation) {
            return new ReservationList(reservation.getReservationId(),
                reservation.getReservationNumber(),
                reservation.getUserName(), reservation.getReservationDate(),
                reservation.getReservationDateTime(), reservation.getPhoneNumber(),
                reservation.getApproval());
        }
    }
}
