package com.store.reservation.domain.review.entity;

import com.store.reservation.domain.base.BaseEntity;
import com.store.reservation.domain.reservation.entity.Reservation;
import com.store.reservation.domain.restaurant.entity.Restaurant;
import com.store.reservation.domain.review.model.ReviewForm;
import com.store.reservation.domain.user.normal.entity.Normal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "review")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class Review extends BaseEntity {

    /**
     * 리뷰 아이디
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewid;

    /**
     * 내용
     */
    @Column(length = 500)
    private String content;

    /**
     * 평점
     */
    @Column(precision = 2, scale = 1)
    @Digits(integer = 1, fraction = 1)
    private double rating;


    /**
     * Normal 와 조인
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "normal_id")
    private Normal normal;

    /**
     * Restaurant 과 조인
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    /**
     * Reservation 과 조인
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;


    /**
     * ReviewForm -> Review
     *
     * @param reviewForm 리뷰 형식
     * @return Review
     */
    public static Review from(ReviewForm reviewForm) {
        return Review.builder()
            .content(reviewForm.getContent())
            .rating(reviewForm.getRating())
            .build();
    }

}
