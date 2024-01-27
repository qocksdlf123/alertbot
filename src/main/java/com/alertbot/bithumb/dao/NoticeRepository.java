package com.alertbot.bithumb.dao;

import com.alertbot.bithumb.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

}
