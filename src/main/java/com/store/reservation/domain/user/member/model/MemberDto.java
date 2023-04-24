package com.store.reservation.domain.user.member.model;

import com.store.reservation.domain.restaurant.entity.Restaurant;
import com.store.reservation.domain.restaurant.model.RestaurantDto;
import com.store.reservation.domain.user.common.UserType;
import com.store.reservation.domain.user.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long userid;
    private UserType userClass;
    private String userEmail;
    private String password;
    private String name;
    private String phoneNumber;

    private List<RestaurantDto> restaurantList = new ArrayList<>();

    /**
     * Member -> MemberDto
     *
     * @param member Member 객체
     * @return MemberDto
     */
    public static MemberDto from(Member member) {
        List<RestaurantDto> restaurants = forList(member.getRestaurantList());

        return new MemberDto(member.getUserid(), member.getUserClass(), member.getUserEmail(),
            member.getPassword(), member.getName(), member.getPhoneNumber(), restaurants);
    }

    /**
     * List<Restaurant> -> List<RestaurantDto> 변환
     *
     * @param restaurantList List<Restaurant> 리스트
     * @return List<RestaurantDto>
     */
    private static List<RestaurantDto> forList(List<Restaurant> restaurantList) {
        List<RestaurantDto> restaurantDtoList = new ArrayList<>();

        for (Restaurant restaurant : restaurantList) {
            restaurantDtoList.add(RestaurantDto.from(restaurant));
        }

        return restaurantDtoList;
    }
}
