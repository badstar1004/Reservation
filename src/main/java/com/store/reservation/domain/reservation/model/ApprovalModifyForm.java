package com.store.reservation.domain.reservation.model;

import com.store.reservation.domain.reservation.common.ApprovalType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalModifyForm {

    private List<String> reservationNumber;
    private ApprovalType approvalType;

}
