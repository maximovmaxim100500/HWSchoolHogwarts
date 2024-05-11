package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
public class InfoController {
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/port")
    public String getServerPort() {
        return "Порт приложения: " + serverPort;
    }

    @GetMapping("/sum")
    public Integer getSum() {
        int sum = IntStream.rangeClosed(1, 1000_000)
                .parallel()
                .sum();
        return sum;
    }
}

