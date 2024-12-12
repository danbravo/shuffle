package com.example.shuffle.controller;

import com.example.shuffle.util.ShuffleUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Arrays;

@RestController
@RequestMapping("/shuffle")
public class ShuffleController {

    private final WebClient webClient;

    @Value("${log.service.url}")
    private String logServiceUrl;

    public ShuffleController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PostMapping
    public ResponseEntity<int[]> shuffle(@RequestParam @Min(1) @Max(1000) int number) {
        int[] shuffledArray = ShuffleUtil.shuffleArray(number);

        // Utilizing WebClient to send async log request
        webClient.post()
                .uri(logServiceUrl + "/log")
                .bodyValue("Generated array for number: " + number + " -> " + Arrays.toString(shuffledArray))
                .retrieve()
                .toBodilessEntity()
                .subscribe();

        return ResponseEntity.ok(shuffledArray);
    }
}
