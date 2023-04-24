package com.store.reservation.domain.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckReservationForm {

    private String reservationNumber;
    private String phoneNumber;
}
