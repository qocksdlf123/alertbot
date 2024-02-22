package com.alertbot.upbit.service;

import com.alertbot.kakao.KakaoMSGService;
import com.alertbot.upbit.dao.UpbitNoticeRepository;
import com.alertbot.upbit.domain.UpbitNotice;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpbitParsingService {

    private final UpbitNoticeRepository noticeRepository;

    private final KakaoMSGService kakaoMSGService;


    @PostConstruct
    public void init(){
        WebDriverManager.chromedriver().driverVersion("121.0.6167.85").setup();
    }
    @Scheduled(cron = "0 */10 * * * *") //10분마다 실행
    @Async
    public void crawlUpbitNotices() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(options);

//        // Upbit 공지사항 페이지 접속
        driver.get("https://upbit.com/service_center/notice");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(60 * 1000));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-12ct4qh")));
        // 공지사항 목록을 가져옵니다.
        List<WebElement> elements = driver.findElements(By.cssSelector(".css-12ct4qh"));
        Optional<UpbitNotice> topNotice = noticeRepository.findTopByOrderByIdDesc();
        Integer topNoticeNumber;
        if (topNotice.isPresent()) {
            topNoticeNumber = topNotice.get().getNoticeNumber();
        } else {
            topNoticeNumber = 0;
        }
        // 각 공지사항에 대하여
        for (WebElement noticeElement : elements) {
            // 공지사항의 제목과 링크를 가져옵니다.
            WebElement anchorElement1 = noticeElement.findElement(By.cssSelector("a > span"));
            String title = anchorElement1.getText(); // "a" 태그의 텍스트(제목)를 가져옵니다.
            String link = noticeElement.getAttribute("href"); // "a" 태그의 "href" 속성(링크)를 가져옵니다.
            Integer noticeNumber = Integer.parseInt(link.substring(link.length() - 4));
            // 제목과 링크 출력
            System.out.println("title = " + title);
            System.out.println("link = " + link);
            if (noticeNumber > topNoticeNumber && (title.contains("마켓 디지털 자산 추가") || title.contains("입출금"))) {
                topNoticeNumber = noticeNumber;
                UpbitNotice upbitNotice = UpbitNotice.builder()
                        .title(title)
                        .noticeNumber(noticeNumber)
                        .build();

                noticeRepository.save(upbitNotice);

                String accessToken = kakaoMSGService.accessTokenReissue();
                kakaoMSGService.sendMeMSG(title,link,accessToken);

            }

        }
            // WebDriver 종료
            driver.quit();
    }
}
