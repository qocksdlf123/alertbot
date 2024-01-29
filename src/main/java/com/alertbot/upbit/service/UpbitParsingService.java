package com.alertbot.upbit.service;

import com.alertbot.kakao.KakaoMSGService;
import com.alertbot.upbit.dao.UpbitNoticeRepository;
import com.alertbot.upbit.domain.UpbitNotice;
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
public class UpbitParsingService {

    private final UpbitNoticeRepository noticeRepository;

    private final KakaoMSGService kakaoMSGService;

    @Scheduled(cron = "0 */5 * * * *") //5분마다 실행
//    @Scheduled(cron = "")
    public void parseUpbit() {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.upbit.com/service_center/notice")
                    .timeout(6 * 1000)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = doc.select("tr > td > a");
        System.out.println(doc);
        Optional<UpbitNotice> topNotice = noticeRepository.findTopByOrderByIdDesc();
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
            System.out.println(title);
            Integer noticeNumber = Integer.parseInt(element.toString().substring(17,24));
            System.out.println(noticeNumber);
/*            if(noticeNumber > topNoticeNumber  &&(title.contains("마켓 추가") || title.contains("입출금"))){
                topNoticeNumber = noticeNumber;
                UpbitNotice notice = UpbitNotice.builder()
                        .title(title)
                        .noticeNumber(noticeNumber)
                        .build();
                noticeRepository.save(notice);

                String accessToken = kakaoMSGService.accessTokenReissue();
                kakaoMSGService.sendMeMSG(title,"https://feed.Upbit.com/notice/"+noticeNumber+"?list_params=",accessToken);


                System.out.println(title);
                System.out.println(noticeNumber);
            }*/
        }
    }
}
