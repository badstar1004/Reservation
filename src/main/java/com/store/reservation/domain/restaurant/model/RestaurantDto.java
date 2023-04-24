package com.store.reservation.domain.restaurant.model;

import com.store.reservation.domain.restaurant.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {

    private Long restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantTelNumber;
    private int tableCount;


    /**
     * restaurant -> RestaurantDto
     *
     * @param restaurant restaurant 객체
     * @return RestaurantDto
     */
    public static RestaurantDto from(Restaurant restaurant) {
        return new RestaurantDto(restaurant.getRestaurantId(), restaurant.getRestaurantName(),
            restaurant.getRestaurantAddress(), restaurant.getRestaurantTelNumber(),
            restaurant.getTableCount());
    }
}
