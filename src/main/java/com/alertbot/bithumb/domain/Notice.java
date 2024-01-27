package com.alertbot.bithumb.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer noticeId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "NOTICE_NUMBER")
    private String noticeNumber;





}
