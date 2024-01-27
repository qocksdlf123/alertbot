package com.alertbot.bithumb.service;

import com.alertbot.bithumb.dao.NoticeRepository;
import com.alertbot.bithumb.domain.Notice;
import com.alertbot.kakao.KakaoMSGService;
import com.alertbot.kakao.dto.ReissueResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BithumbParsingService {

    private final NoticeRepository noticeRepository;

    private final KakaoMSGService kakaoMSGService;

    @Scheduled(cron = "0 */5 * * * *") //5분마다 실행
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
            String title = spans.text();
            Optional<Notice> topNotice = noticeRepository.findTopByOrderByIdDesc();
            String topTitle;
            if(topNotice.isPresent()){
                topTitle = topNotice.get().getTitle();
            }else{
                topTitle = "";
            }

            if(!title.equals(topTitle) &&(title.contains("마켓 추가") || title.contains("입출금"))){
                String noticeNumber = element.toString().substring(17,24);
                Notice notice = Notice.builder()
                        .title(title)
                        .noticeNumber(noticeNumber)
                        .build();
                noticeRepository.save(notice);

                String accessToken = kakaoMSGService.accessTokenReissue();
                kakaoMSGService.sendMeMSG(title,"https://feed.bithumb.com/notice/"+noticeNumber+"?list_params=",accessToken);


                System.out.println(title);
                System.out.println(noticeNumber);
            }
        }
    }
}
