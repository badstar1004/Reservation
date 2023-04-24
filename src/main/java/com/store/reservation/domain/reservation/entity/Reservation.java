package com.store.reservation.domain.reservation.entity;

import com.store.reservation.domain.base.BaseEntity;
import com.store.reservation.domain.reservation.common.ApprovalType;
import com.store.reservation.domain.reservation.model.ReservationForm;
import com.store.reservation.domain.restaurant.entity.Restaurant;
import com.store.reservation.domain.review.entity.Review;
import com.store.reservation.domain.user.member.entity.Member;
import com.store.reservation.domain.user.normal.entity.Normal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "Reservation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class Reservation extends BaseEntity {

    /**
     * 예약 아이디
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    /**
     * 예약번호
     */
    @Column(name = "reservation_number")
    private String reservationNumber;

    /**
     * 예약자
     */
    @Column(name = "name", length = 20)
    private String userName;

    /**
     * 예약 날짜
     */
    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    /**
     * 예약 시간
     */
    @Column(name = "reservation_time", columnDefinition = "DATETIME")
    private LocalDateTime reservationDateTime;

    /**
     * 예약자 전화번호
     */
    @Column(name = "phone_number", length = 13)
    private String phoneNumber;

    /**
     * 승인상태
     */
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ApprovalType approval;

    /**
     * Member 와 조인
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * Normal 와 조인
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "normal_id")
    private Normal normal;

    /**
     * Restaurant 과 조인
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    /**
     * 리뷰 정보
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private List<Review> reviewList = new ArrayList<>();


    /**
     * ReservationForm -> Reservation
     *
     * @param reservationForm 예약 등록 폼
     * @return Reservation
     */
    public static Reservation of(Normal normal, ReservationForm reservationForm, String number) {
        return Reservation.builder()
            .userName(normal.getName())
            .reservationNumber(number)
            .reservationDate(reservationForm.getReservationDate())
            .reservationDateTime(reservationForm.getReservationDateTime())
            .phoneNumber(normal.getPhoneNumber())
            .approval(ApprovalType.STANDBY)
            .build();
    }
}
