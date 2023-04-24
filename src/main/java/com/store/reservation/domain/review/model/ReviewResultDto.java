package com.store.reservation.domain.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResultDto {

    private Long reviewId;
    private String Name;
    private String contents;
    private double rating;

    private Long reservationId;
    private String reservationNumber;

    private Long restaurantId;
    private String restaurantName;


    /**
     * ReviewDto -> ReviewResultDto
     *
     * @param reviewDto reviewDto
     * @return ReviewResultDto
     */
    public static ReviewResultDto from(ReviewDto reviewDto) {
        return new ReviewResultDto(reviewDto.getReviewid(), reviewDto.getNormal().getName(),
            reviewDto.getContent(), reviewDto.getRating(),
            reviewDto.getReservation().getReservationId(),
            reviewDto.getReservation().getReservationNumber(),
            reviewDto.getRestaurant().getRestaurantId(),
            reviewDto.getRestaurant().getRestaurantName());
    }
}
