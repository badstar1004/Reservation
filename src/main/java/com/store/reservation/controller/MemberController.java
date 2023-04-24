package com.store.reservation.controller;

import static com.store.reservation.domain.user.common.UserType.MEMBER;
import static com.store.reservation.exception.ErrorCode.NORMAL_CLASS;

import com.store.reservation.domain.reservation.model.ApprovalModifyForm;
import com.store.reservation.domain.reservation.model.ResRestaurantList;
import com.store.reservation.domain.restaurant.model.ResRestaurantForm;
import com.store.reservation.domain.user.UserSignIn;
import com.store.reservation.domain.user.UserSignUp;
import com.store.reservation.domain.user.common.UserType;
import com.store.reservation.domain.user.member.model.MemberDto;
import com.store.reservation.exception.CustomException;
import com.store.reservation.service.MemberService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 (매장 매니저)
     *
     * @param userSignUp 회원가입 형식
     * @return String
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUp userSignUp) {
        String message = memberService.saveUser(userSignUp);
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
        String message = memberService.loginAndToken(userSignIn);
        return ResponseEntity.ok(message);
    }

    /**
     * 정보 조회
     *
     * @param userid user 아이디
     * @return UserDto
     */
    @GetMapping("/{userid}")
    public ResponseEntity<MemberDto> getUserInfo(@PathVariable Long userid) {

        MemberDto memberDto = memberService.userInfo(userid);
        return ResponseEntity.ok(memberDto);
    }

    /**
     * 매장 등록 (매니저)
     *
     * @param userid            user 아이디
     * @param resRestaurantForm 매장 등록 폼
     * @return User
     */
    @PostMapping("/{userid}/restaurant")
    public ResponseEntity<MemberDto> resRestaurant(@PathVariable Long userid,
        @RequestBody ResRestaurantForm resRestaurantForm) {

        // 회원구분 (MEMBER) 확인
        UserType userType = memberService.userInfo(userid).getUserClass();

        if (!userType.equals(MEMBER)) {
            throw new CustomException(NORMAL_CLASS);
        }

        return ResponseEntity.ok(
            MemberDto.from(memberService.addRestaurant(userid, resRestaurantForm)));
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
        UserType userType = memberService.userInfo(userid).getUserClass();

        return ResponseEntity.ok(memberService.searchReservation(userid, userType));
    }

    /**
     * 예약 승인/취소
     *
     * @param userid             user 아이디
     * @param approvalModifyForm 예약 승인/취소 폼
     * @return String
     */
    @PutMapping("/{userid}/reservation/approval")
    public ResponseEntity<String> updateApproval(@PathVariable Long userid,
        @RequestBody ApprovalModifyForm approvalModifyForm) {
        return ResponseEntity.ok(memberService.updateApproval(approvalModifyForm));
    }
}
