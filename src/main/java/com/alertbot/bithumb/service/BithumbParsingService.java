package com.alertbot.bithumb.service;

import com.alertbot.bithumb.dao.NoticeRepository;
import com.alertbot.kakao.KakaoMSGService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Slf4j
@Service
@RequiredArgsConstructor
public class BithumbParsingService {

    private final NoticeRepository noticeRepository;

    private final KakaoMSGService kakaoMSGService;
//    @Scheduled(cron = "")
    public void parseBithumb() {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://feed.bithumb.com/notice")
                    .timeout(6 * 1000)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = doc.select("li > a");

        for (Element element: elements) {
            System.out.println(element);
            Elements spans = element.select("span");
            String s = spans.text();
            if(s.contains("마켓 추가")){



                System.out.println(s);
                System.out.println(element.toString().substring(17,24));
            }
        }
    }
}
