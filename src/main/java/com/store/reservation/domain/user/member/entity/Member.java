package com.store.reservation.domain.user.member.entity;

import com.store.reservation.domain.base.BaseEntity;
import com.store.reservation.domain.reservation.entity.Reservation;
import com.store.reservation.domain.restaurant.entity.Restaurant;
import com.store.reservation.domain.user.UserSignUp;
import com.store.reservation.domain.user.common.UserType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "Member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity {

    /**
     * 회원 아이디
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userid;

    /**
     * 회원구분
     */
    @Column(name = "user_class", length = 20)
    @Enumerated(EnumType.STRING)
    private UserType userClass;

    /**
     * 회원 이메일
     */
    @Column(name = "user_email", length = 100)
    @Email(message = "이메일 다시 입력.")
    private String userEmail;

    /**
     * 비밀번호
     */
    @Column(length = 20)
    private String password;

    /**
     * 이름
     */
    @Column(length = 20)
    private String name;

    /**
     * 핸드폰번호
     */
    @Column(name = "phone_number", length = 13)
    private String phoneNumber;


    /**
     * 매장 정보
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Restaurant> restaurantList = new ArrayList<>();

    /**
     * 예약 정보
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Reservation> reservationList = new ArrayList<>();


    /**
     * accountSignUp -> User
     *
     * @param userSignUp 회원가입 형식
     * @return User
     */
    public static Member from(UserSignUp userSignUp) {
        return Member.builder()
            .userClass(userSignUp.getUserClass())
            .userEmail(userSignUp.getUserEmail().toLowerCase(Locale.ROOT))
            .password(userSignUp.getPassword())
            .name(userSignUp.getName())
            .phoneNumber(userSignUp.getPhoneNumber())
            .build();
    }
}
