package com.store.reservation.domain.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResRestaurantForm {

    private String restaurantName;
    private String restaurantAddress;
    private String restaurantTelNumber;
    private int tableCount;

}
