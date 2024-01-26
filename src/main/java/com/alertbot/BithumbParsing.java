package com.alertbot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class BithumbParsing {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://feed.bithumb.com/notice")
                .timeout(6 * 1000)
                .get();
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
