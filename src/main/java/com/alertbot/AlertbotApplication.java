package com.alertbot;

		import com.alertbot.bithumb.service.BithumbParsingService;
		import com.alertbot.kakao.KakaoMSGService;
		import org.springframework.boot.SpringApplication;
		import org.springframework.boot.autoconfigure.SpringBootApplication;
		import org.springframework.context.ConfigurableApplicationContext;
		import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlertbotApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AlertbotApplication.class, args);
		BithumbParsingService bithumbParsingService = context.getBean(BithumbParsingService.class);
		bithumbParsingService.parseBithumb();
		KakaoMSGService kakaoMSGService = context.getBean(KakaoMSGService.class);
//		kakaoMSGService.sendMeMSG("빗썸","https://feed.bithumb.com/notice/1644422?list_params=page%3D2","3y4Eo4Syn3HqTQ-hqhKofGrQSW8h8W3sIcQKKwzTAAABjUSRvn7okopMIboAuA");
//		kakaoMSGService.accessTokenReissue();

	}

}
