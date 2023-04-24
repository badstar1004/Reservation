package com.store.reservation.service;

import static com.store.reservation.exception.ErrorCode.ALREADY_REGISTER_USER;
import static com.store.reservation.exception.ErrorCode.LOGIN_CHECK_FAIL;
import static com.store.reservation.exception.ErrorCode.NOT_A_USE_RESERVATION_NUMBER;
import static com.store.reservation.exception.ErrorCode.NOT_FOUND_USER;
import static com.store.reservation.exception.ErrorCode.NO_CORRESPONDING_DATA_RESERVATION_NUMBER;
import static com.store.reservation.exception.ErrorCode.NO_RESTAURANT_REGISTERED;

import com.store.reservation.config.jwt.JwtAuthenticationProvider;
import com.store.reservation.domain.reservation.common.ApprovalType;
import com.store.reservation.domain.reservation.entity.Reservation;
import com.store.reservation.domain.reservation.model.ResRestaurantList;
import com.store.reservation.domain.reservation.model.ReservationForm;
import com.store.reservation.domain.restaurant.entity.Restaurant;
import com.store.reservation.domain.review.entity.Review;
import com.store.reservation.domain.review.model.ReviewForm;
import com.store.reservation.domain.user.UserSignIn;
import com.store.reservation.domain.user.UserSignUp;
import com.store.reservation.domain.user.common.UserType;
import com.store.reservation.domain.user.member.entity.Member;
import com.store.reservation.domain.user.normal.entity.Normal;
import com.store.reservation.domain.user.normal.model.NormalDto;
import com.store.reservation.exception.CustomException;
import com.store.reservation.repository.MemberRepository;
import com.store.reservation.repository.NormalRepository;
import com.store.reservation.repository.ReservationRepository;
import com.store.reservation.repository.RestaurantRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NormalService {

    private final JwtAuthenticationProvider jwtProvider;
    private final NormalRepository normalRepository;
    private final RestaurantRepository restaurantRepository;
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
            normalRepository.save(Normal.from(userSignUp));
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
        return normalRepository.findByUserEmail(userEmail.toLowerCase(Locale.ROOT))
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
        Normal chkNormal = normalRepository.findByUserEmail(userSignIn.getUserEmail())
            .stream().filter(
                normal -> normal.getPassword().equals(userSignIn.getPassword())
            ).findFirst()
            .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        // 토큰 발행
        return jwtProvider.createToken(chkNormal.getUserid(), chkNormal.getUserEmail(),
            chkNormal.getUserClass());
    }

    /**
     * 회원정보 조회
     *
     * @param userid user 아이디
     * @return UserDto
     */
    public NormalDto userInfo(Long userid) {
        Normal normal = normalRepository.findById(userid)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        return NormalDto.from(normal);
    }

    /**
     * 매장 예약
     *
     * @param userid          user 아이디
     * @param reservationForm 예약 등록 폼
     * @return NormalDto
     */
    @Transactional
    public Normal addReservation(Long userid, Long restaurantid,
        ReservationForm reservationForm) {

        // 회원이 있는지 확인
        Normal normal = normalRepository.findById(userid)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        // 해당 매장이 있는지 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantid)
            .orElseThrow(() -> new CustomException(NO_RESTAURANT_REGISTERED));

        // 매니저 아이디
        Long memberId = restaurant.getMember().getUserid();

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        // 예약번호 생성
        String number = reservationNumber();

        Reservation reservation = Reservation.of(normal, reservationForm, number);

        // 예약 저장 (일반회원)
        normal.getReservationList().add(reservation);
        // 예약 저장 (매니저)
        member.getReservationList().add(reservation);
        // 예약 저장 (매장)
        restaurant.getReservationList().add(reservation);

        return normal;
    }

    /**
     * 예약번호 생성
     *
     * @return String
     */
    private String reservationNumber() {

        // 당일 예약번호
        LocalDate date = LocalDate.now(); // 현재 날짜
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); // 포맷 지정
        String formattedDate = date.format(formatter); // 문자열 생성

        String number = reservationRepository.findMaxReservationNumber(formattedDate)
            .orElse("");

        if (number == "") {
            number = formattedDate + "001";

        } else {
            StringBuilder sb = new StringBuilder();
            int lastNum = Integer.parseInt(number.substring(9));
            String nextNumber = String.format("%03d", lastNum + 1);
            sb.append(formattedDate).append(nextNumber);
            number = sb.toString();
        }

        return number;
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
            reservationRepository.findNormalReservation(userid, userType);

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
     * 리뷰 등록
     *
     * @param userid            user 아이디
     * @param restaurantId      매장 아이디
     * @param reservationNumber 예약번호
     * @param reviewForm        리뷰 폼
     * @return ReviewDto
     */
    @Transactional
    public Review addReview(Long userid, Long restaurantId, String reservationNumber,
        ReviewForm reviewForm) {
        Normal normal = normalRepository.findById(userid)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new CustomException(NO_RESTAURANT_REGISTERED));

        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber)
            .orElseThrow(() -> new CustomException(NO_CORRESPONDING_DATA_RESERVATION_NUMBER));

        // 예약상태 확인
        if (!reservation.getApproval().equals(ApprovalType.USED)) {
            throw new CustomException(NOT_A_USE_RESERVATION_NUMBER);
        }

        Review review = Review.from(reviewForm);

        // 리뷰 저장
        normal.getReviewList().add(review);
        reservation.getReviewList().add(review);
        restaurant.getReviewList().add(review);

        return review;
    }

}
