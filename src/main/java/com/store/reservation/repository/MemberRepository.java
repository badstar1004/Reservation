package com.store.reservation.repository;

import com.store.reservation.domain.user.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * email 조회
     *
     * @param userEmail 이메일
     * @return Optional
     */
    Optional<Member> findByUserEmail(String userEmail);
}
