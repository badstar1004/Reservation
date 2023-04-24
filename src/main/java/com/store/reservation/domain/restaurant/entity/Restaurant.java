package com.store.reservation.domain.restaurant.entity;

import com.store.reservation.domain.base.BaseEntity;
import com.store.reservation.domain.reservation.entity.Reservation;
import com.store.reservation.domain.restaurant.model.ResRestaurantForm;
import com.store.reservation.domain.review.entity.Review;
import com.store.reservation.domain.user.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "Restaurant")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class Restaurant extends BaseEntity {

    /**
     * 매장 아이디
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long restaurantId;

    /**
     * 상호명
     */
    @Column(name = "restaurant_name", length = 50)
    private String restaurantName;

    /**
     * 매장 주소
     */
    @Column(name = "restaurant_address", length = 100)
    private String restaurantAddress;

    /**
     * 매장 전화번호
     */
    @Column(name = "restaurant_telnumber", length = 14)
    private String restaurantTelNumber;

    /**
     * 테이블 수
     */
    @Column(name = "table_count")
    private int tableCount;


    /**
     * Member 와 조인
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Member member;

    /**
     * 예약 정보
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    private List<Reservation> reservationList = new ArrayList<>();

    /**
     * 리뷰 정보
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    private List<Review> reviewList = new ArrayList<>();

    /**
     * resRestaurantForm -> Restaurant
     *
     * @param resRestaurantForm 매장 등록폼
     * @return Restaurant
     */
    public static Restaurant from(ResRestaurantForm resRestaurantForm) {
        return Restaurant.builder()
            .restaurantName(resRestaurantForm.getRestaurantName())
            .restaurantAddress(resRestaurantForm.getRestaurantAddress())
            .restaurantTelNumber(resRestaurantForm.getRestaurantTelNumber())
            .tableCount(resRestaurantForm.getTableCount())
            .build();
    }
}
