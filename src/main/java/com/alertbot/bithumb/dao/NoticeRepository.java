package com.alertbot.bithumb.dao;

import com.alertbot.bithumb.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    @Query("SELECT n FROM Notice n WHERE n.noticeId = (SELECT MAX(n.noticeId) FROM Notice e)")
    Optional<Notice> findTopByOrderByIdDesc();

}
