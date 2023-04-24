package com.store.reservation.repository;

import com.store.reservation.domain.reservation.entity.Reservation;
import com.store.reservation.domain.user.common.UserType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * user 아이디 기준 예약 조회
     *
     * @param userid   회원 아이디 (매니저)
     * @param userType 회원 구분
     * @return List<Reservation>
     */
    @Query("SELECT r FROM Reservation r WHERE r.member.userid = :userid AND r.member.userClass = :userType ORDER BY r.reservationDate")
    List<Reservation> findMemberReservation(@Param("userid") Long userid,
        @Param("userType") UserType userType);

    /**
     * user 아이디 기준 예약 조회
     *
     * @param userid   회원 아이디 (일반회원)
     * @param userType 회원 구분
     * @return List<Reservation>
     */
    @Query("SELECT r FROM Reservation r WHERE r.normal.userid = :userid AND r.normal.userClass = :userType ORDER BY r.reservationDate")
    List<Reservation> findNormalReservation(
        @Param("userid") Long userid, @Param("userType") UserType userType);

    /**
     * 예약번호 생성시 예약번호 조회 (최대값)
     *
     * @param prefix
     * @return
     */
    @Query(value = "SELECT MAX(reservation_number) FROM reservation WHERE reservation_number LIKE :prefix%",
        nativeQuery = true)
    Optional<String> findMaxReservationNumber(@Param("prefix") String prefix);


    /**
     * List 형 예약번호들의 정보 조회
     *
     * @param reservationNumbers 예약번호 리스트
     * @return List<Reservation>
     */
    @Query("SELECT r FROM Reservation r WHERE r.reservationNumber IN :reservationNumbers")
    List<Reservation> findByReservationNumberIn(
        @Param("reservationNumbers") List<String> reservationNumbers);


    /**
     * 예약정보 조회 (매장 아이디, 예약번호, 폰번호 기준)
     *
     * @param restaurantId
     * @param reservationNumber
     * @param phoneNumber
     * @return Reservation
     */
    @Query("SELECT r FROM Reservation r WHERE r.restaurant.restaurantId = :restaurantId AND r.reservationNumber = :reservationNumber AND r.phoneNumber = :phoneNumber")
    Optional<Reservation> findReservation(
        @Param("restaurantId") Long restaurantId,
        @Param("reservationNumber") String reservationNumber,
        @Param("phoneNumber") String phoneNumber);


    /**
     * 예약정보 조회 (예약번호 기준)
     *
     * @param reservationNumber 예약번호
     * @return Reservation
     */
    Optional<Reservation> findByReservationNumber(String reservationNumber);
}
