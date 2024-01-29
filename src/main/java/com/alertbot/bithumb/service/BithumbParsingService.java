package com.alertbot.bithumb.service;

import com.alertbot.bithumb.dao.BithumbNoticeRepository;
import com.alertbot.bithumb.domain.BithumbNotice;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BithumbParsingService {

    private final BithumbNoticeRepository noticeRepository;

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
        Elements elements = doc.select("main > ul > li > a");

        Optional<BithumbNotice> topNotice = noticeRepository.findTopByOrderByIdDesc();
        Integer topNoticeNumber;
        if(topNotice.isPresent()){
            topNoticeNumber = topNotice.get().getNoticeNumber();
        }else{
            topNoticeNumber = 0;
        }

        for (Element element: elements) {
//            System.out.println(element);
            Elements spans = element.select("span");
            String title = spans.text();

            Integer noticeNumber = Integer.parseInt(element.toString().substring(17,24));
            if(noticeNumber > topNoticeNumber  &&(title.contains("마켓 추가") || title.contains("입출금"))){
                topNoticeNumber = noticeNumber;
                BithumbNotice notice = BithumbNotice.builder()
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
