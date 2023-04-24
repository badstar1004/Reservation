package com.store.reservation.controller;

import static com.store.reservation.domain.user.common.UserType.NORMAL;
import static com.store.reservation.exception.ErrorCode.MEMBER_CLASS;

import com.store.reservation.domain.reservation.model.ResRestaurantList;
import com.store.reservation.domain.reservation.model.ReservationForm;
import com.store.reservation.domain.review.model.ReviewDto;
import com.store.reservation.domain.review.model.ReviewForm;
import com.store.reservation.domain.review.model.ReviewResultDto;
import com.store.reservation.domain.user.UserSignIn;
import com.store.reservation.domain.user.UserSignUp;
import com.store.reservation.domain.user.common.UserType;
import com.store.reservation.domain.user.normal.model.NormalDto;
import com.store.reservation.exception.CustomException;
import com.store.reservation.service.NormalService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/normal")
public class NormalController {

    private final NormalService normalService;

    /**
     * 회원가입 (매장 사용자)
     *
     * @param userSignUp 회원가입 형식
     * @return String
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUp userSignUp) {
        String message = normalService.saveUser(userSignUp);
        return ResponseEntity.ok(message);
    }

    /**
     * 로그인 (토큰 발행)
     *
     * @param userSignIn 로그인 형식
     * @return String
     */
    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@Valid @RequestBody UserSignIn userSignIn) {
        String message = normalService.loginAndToken(userSignIn);
        return ResponseEntity.ok(message);
    }

    /**
     * 정보 조회
     *
     * @param userid user 아이디
     * @return UserDto
     */
    @GetMapping("/{userid}")
    public ResponseEntity<NormalDto> getUserInfo(@PathVariable Long userid) {

        NormalDto normalDto = normalService.userInfo(userid);
        return ResponseEntity.ok(normalDto);
    }

    /**
     * 매장 예약
     *
     * @param userid          user 아이디
     * @param restaurantid    매장 아이디
     * @param reservationForm 예약 폼
     * @return NormalDto
     */
    @PostMapping("/{userid}/reservation/{restaurantid}")
    public ResponseEntity<NormalDto> ResReservation(@PathVariable Long userid,
        @PathVariable Long restaurantid, @RequestBody ReservationForm reservationForm) {

        // 회원구분 (NORMAL) 확인
        UserType userType = normalService.userInfo(userid).getUserClass();

        if (!userType.equals(NORMAL)) {
            throw new CustomException(MEMBER_CLASS);
        }

        return ResponseEntity.ok(
            NormalDto.from(normalService.addReservation(userid, restaurantid, reservationForm)));
    }

    /**
     * 매장 예약 리스트
     *
     * @param userid user 아이디
     * @return List<ReservationDto>
     */
    @GetMapping("/{userid}/restaurant/reservation-list")
    public ResponseEntity<List<ResRestaurantList>> getReservationList(@PathVariable Long userid) {

        // 회원구분
        UserType userType = normalService.userInfo(userid).getUserClass();

        return ResponseEntity.ok(normalService.searchReservation(userid, userType));
    }

    /**
     * 리뷰 등록
     *
     * @param userid            user 아이디
     * @param restaurantid      매장 아이디
     * @param reservationnumber 예약번호
     * @param reviewForm        리뷰 폼
     * @return ReviewDto
     */
    @PostMapping("/{userid}/{restaurantid}/{reservationnumber}/review")
    public ResponseEntity<ReviewResultDto> ResReview(@PathVariable Long userid,
        @PathVariable Long restaurantid,
        @PathVariable String reservationnumber,
        @RequestBody ReviewForm reviewForm) {

        ReviewDto reviewDto =
            ReviewDto.from(
                normalService.addReview(userid, restaurantid, reservationnumber, reviewForm));

        return ResponseEntity.ok(ReviewResultDto.from(reviewDto));
    }
}
