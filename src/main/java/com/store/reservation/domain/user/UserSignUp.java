package com.store.reservation.domain.user;

import com.store.reservation.domain.user.common.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUp {

    private UserType userClass;
    private String userEmail;
    private String password;
    private String name;
    private String phoneNumber;
}
