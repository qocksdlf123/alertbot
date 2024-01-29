package com.alertbot.bithumb.dao;

import com.alertbot.bithumb.domain.BithumbNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BithumbNoticeRepository extends JpaRepository<BithumbNotice, Integer> {
    @Query("SELECT n FROM BithumbNotice n WHERE n.noticeId = (SELECT MAX(n.noticeId) FROM BithumbNotice e)")
    Optional<BithumbNotice> findTopByOrderByIdDesc();

}
