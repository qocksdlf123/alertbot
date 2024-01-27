package com.alertbot.bithumb.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Notice {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer noticeId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "NOTICE_NUMBER")
    private String noticeNumber;





}
