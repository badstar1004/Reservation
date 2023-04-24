package com.store.reservation.service;

import static com.store.reservation.exception.ErrorCode.ALREADY_REGISTER_USER;
import static com.store.reservation.exception.ErrorCode.LOGIN_CHECK_FAIL;
import static com.store.reservation.exception.ErrorCode.NOT_FOUND_USER;
import static com.store.reservation.exception.ErrorCode.NO_CORRESPONDING_DATA_RESERVATION_NUMBER;
import static com.store.reservation.exception.ErrorCode.SAME_RESTAURANT_NAME;

import com.store.reservation.config.jwt.JwtAuthenticationProvider;
import com.store.reservation.domain.reservation.common.ApprovalType;
import com.store.reservation.domain.reservation.entity.Reservation;
import com.store.reservation.domain.reservation.model.ApprovalModifyForm;
import com.store.reservation.domain.reservation.model.ResRestaurantList;
import com.store.reservation.domain.restaurant.entity.Restaurant;
import com.store.reservation.domain.restaurant.model.ResRestaurantForm;
import com.store.reservation.domain.user.UserSignIn;
import com.store.reservation.domain.user.UserSignUp;
import com.store.reservation.domain.user.common.UserType;
import com.store.reservation.domain.user.member.entity.Member;
import com.store.reservation.domain.user.member.model.MemberDto;
import com.store.reservation.exception.CustomException;
import com.store.reservation.repository.MemberRepository;
import com.store.reservation.repository.ReservationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtAuthenticationProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;


    /**
     * 회원정보 저장
     *
     * @param userSignUp 회원가입 정보
     * @return String
     */
    @Transactional
    public String saveUser(UserSignUp userSignUp) {

        // 이메일 중복 확인
        if (isEmailExists(userSignUp.getUserEmail())) {
            throw new CustomException(ALREADY_REGISTER_USER);
        } else {
            memberRepository.save(Member.from(userSignUp));
            return "회원가입이 완료되었습니다.";
        }
    }

    /**
     * 이메일 중복 확인
     *
     * @param userEmail 이메일
     * @return boolean
     */
    private Boolean isEmailExists(String userEmail) {
        return memberRepository.findByUserEmail(userEmail.toLowerCase(Locale.ROOT))
            .isPresent();
    }

    /**
     * 로그인 & 토큰 발행
     *
     * @param userSignIn 로그인 정보
     * @return String
     */
    public String loginAndToken(UserSignIn userSignIn) {

        // 이메일, 비밀번호 확인
        Member chkMember = memberRepository.findByUserEmail(userSignIn.getUserEmail())
            .stream().filter(
                user -> user.getPassword().equals(userSignIn.getPassword())
            ).findFirst()
            .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        // 토큰 발행
        return jwtProvider.createToken(chkMember.getUserid(), chkMember.getUserEmail(),
            chkMember.getUserClass());
    }

    /**
     * 회원정보 조회
     *
     * @param userid user 아이디
     * @return UserDto
     */
    public MemberDto userInfo(Long userid) {
        Member member = memberRepository.findById(userid)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        return MemberDto.from(member);
    }

    /**
     * 매장 저장
     *
     * @param userid            user 아이디
     * @param resRestaurantForm 매장 등록 폼
     * @return User
     */
    @Transactional
    public Member addRestaurant(Long userid, ResRestaurantForm resRestaurantForm) {

        Member member = memberRepository.findById(userid)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        // 상호명이 같은 매장이 있는지 확인
        if (member.getRestaurantList().stream()
            .anyMatch(restaurant ->
                restaurant.getRestaurantName().equals(resRestaurantForm.getRestaurantName()))) {
            throw new CustomException(SAME_RESTAURANT_NAME);
        }

        Restaurant restaurant = Restaurant.from(resRestaurantForm);
        // 매장 저장
        member.getRestaurantList().add(restaurant);

        return member;
    }

    /**
     * 매장 예약 리스트 조회
     *
     * @param userid   user 아이디
     * @param userType 회원구분
     * @return List<ReservationDto>
     */
    public List<ResRestaurantList> searchReservation(Long userid, UserType userType) {

        List<ResRestaurantList> totalList = new ArrayList<>();

        List<Reservation> reservation =
            reservationRepository.findMemberReservation(userid, userType);

        for (Reservation res : reservation) {
            ResRestaurantList restaurantList = new ResRestaurantList();
            // 매장 아이디, 상호명
            restaurantList.setRestaurantId(res.getRestaurant().getRestaurantId());
            restaurantList.setRestaurantName(res.getRestaurant().getRestaurantName());

            // 예약 정보
            List<ResRestaurantList.ReservationList> reservationList = new ArrayList<>();
            reservationList.add(ResRestaurantList.ReservationList.fromList(res));

            restaurantList.setReservationList(reservationList);

            // 반환 list
            totalList.add(restaurantList);
        }

        return totalList;
    }

    /**
     * 예약 승인/취소
     *
     * @param approvalModifyForm 예약 승인/취소 폼
     * @return String
     */
    @Transactional
    public String updateApproval(ApprovalModifyForm approvalModifyForm) {

        List<String> number = approvalModifyForm.getReservationNumber();
        ApprovalType approvalType = approvalModifyForm.getApprovalType();

        List<Reservation> reservationList =
            reservationRepository.findByReservationNumberIn(number);

        // 예약 번호 확인
        if (reservationList.isEmpty()) {
            throw new CustomException(NO_CORRESPONDING_DATA_RESERVATION_NUMBER);
        }

        // 예약 승인/취소 저장
        StringBuilder approvalNum = new StringBuilder();
        for (Reservation reservation : reservationList) {
            approvalNum.append(reservation.getReservationNumber()).append(", ");
            reservation.setApproval(approvalType);
            reservationRepository.save(reservation);
        }

        // 반환값 생성
        String approval = "";
        switch (approvalType) {
            case APPROVAL:
                approval = "승인";
                break;

            case CANCEL:
                approval = "예약취소";
                break;
        }

        String num = approvalNum.toString().replaceAll(",\\s*$", "");
        return "예약번호 : " + num + "\r\n" + approval + "되었습니다.";
    }
}
