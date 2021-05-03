package com.auth.task;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RegisterTask {
    @Value("${register.port}")
    private String registerPort;

    @Value("${register.host}")
    private String registerHost;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.host}")
    private String serverHost;

    @Value("${spring.application.name}")
    private String serverName;

    @EventListener(ApplicationReadyEvent.class)
    public void register(){
        String url = "http://" + registerHost + ":" + registerPort + "/services";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("name", serverName);
        map.put("host", serverHost);
        map.put("port", serverPort);

        try {
            Document asd = Jsoup.connect(url).ignoreHttpErrors(true)
                    .header("Content-Type", "application/json")
                    .data(map)
                    .post();
            System.out.println(asd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
