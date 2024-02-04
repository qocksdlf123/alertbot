package com.alertbot.upbit.dao;

import com.alertbot.bithumb.domain.BithumbNotice;
import com.alertbot.upbit.domain.UpbitNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UpbitNoticeRepository extends JpaRepository<UpbitNotice, Integer> {
    @Query("SELECT n FROM UpbitNotice n WHERE n.noticeId = (SELECT MAX(e.noticeId) FROM UpbitNotice e)")
    Optional<UpbitNotice> findTopByOrderByIdDesc();

}
