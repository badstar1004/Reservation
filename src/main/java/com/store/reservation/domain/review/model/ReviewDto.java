package com.store.reservation.domain.review.model;

import com.store.reservation.domain.reservation.entity.Reservation;
import com.store.reservation.domain.restaurant.entity.Restaurant;
import com.store.reservation.domain.review.entity.Review;
import com.store.reservation.domain.user.normal.entity.Normal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long reviewid;
    private String content;
    private double rating;

    private Normal normal;
    private Reservation reservation;
    private Restaurant restaurant;


    /**
     * Review -> ReviewDto
     *
     * @param review Review
     * @return ReviewDto
     */
    public static ReviewDto from(Review review) {
        return ReviewDto.builder()
            .reviewid(review.getReviewid())
            .content(review.getContent())
            .rating(review.getRating())
            .build();
    }
}
