package com.store.reservation.repository;

import com.store.reservation.domain.user.normal.entity.Normal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NormalRepository extends JpaRepository<Normal, Long> {

    /**
     * email 조회
     *
     * @param userEmail 이메일
     * @return Optional
     */
    Optional<Normal> findByUserEmail(String userEmail);
}
