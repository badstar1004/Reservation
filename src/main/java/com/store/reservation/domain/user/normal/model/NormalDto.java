package com.store.reservation.domain.user.normal.model;

import com.store.reservation.domain.reservation.entity.Reservation;
import com.store.reservation.domain.reservation.model.ReservationDto;
import com.store.reservation.domain.user.common.UserType;
import com.store.reservation.domain.user.normal.entity.Normal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NormalDto {

    private Long userid;
    private UserType userClass;
    private String userEmail;
    private String password;
    private String name;
    private String phoneNumber;

    /**
     * 예약 등록 리스트
     */
    private List<ReservationDto> reservationList = new ArrayList<>();

    /**
     * Normal -> NormalDto
     *
     * @param normal Normal 객체
     * @return NormalDto
     */
    public static NormalDto from(Normal normal) {
        List<ReservationDto> reservationDtoList = forList(normal.getReservationList());

        return new NormalDto(normal.getUserid(), normal.getUserClass(), normal.getUserEmail(),
            normal.getPassword(), normal.getName(), normal.getPhoneNumber(), reservationDtoList);
    }

    /**
     * List<Reservation> -> List<ReservationDto> 변환
     *
     * @param reservationList List<Reservation> 리스트
     * @return List<ReservationDto>
     */
    private static List<ReservationDto> forList(List<Reservation> reservationList) {
        List<ReservationDto> reservationDtoList = new ArrayList<>();

        for (Reservation reservation : reservationList) {
            reservationDtoList.add(ReservationDto.from(reservation));
        }

        return reservationDtoList;
    }
}
