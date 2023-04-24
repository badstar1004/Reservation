package com.store.reservation.controller;

import com.store.reservation.domain.reservation.model.CheckReservationForm;
import com.store.reservation.service.KioskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kiosk")
public class KioskController {

    private final KioskService kioskService;

    /**
     * 예약 번호 확인
     *
     * @param restaurantid         매장 아이디
     * @param checkReservationForm 예약번호 확인 폼
     * @return String
     */
    @GetMapping("/{restaurantid}/reservation-check")
    public ResponseEntity<String> checkReservation(@PathVariable Long restaurantid,
        @RequestBody CheckReservationForm checkReservationForm) {

        return ResponseEntity.ok(kioskService.checkReservation(restaurantid, checkReservationForm));
    }
}
