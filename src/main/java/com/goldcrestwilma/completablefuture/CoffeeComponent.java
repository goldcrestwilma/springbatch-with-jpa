package com.goldcrestwilma.completablefuture;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoffeeComponent implements CoffeeUseCase {

    private final CoffeeRepository coffeeRepository;
    //private final Executor executor = Executors.newFixedThreadPool(10);
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public int getPrice(String name) {
        log.info("동기 호출 방식");
        return coffeeRepository.getPriceByName(name);
    }

    @Override
    public CompletableFuture<Integer> getPriceAsync(String name) {
        log.info("비동기 호출 시작");
        return CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return coffeeRepository.getPriceByName(name);
        }, threadPoolTaskExecutor);
        /*
        CompletableFuture<Integer> future = new CompletableFuture<>();

        new Thread(() -> {
            log.info("새로운 쓰레드 작업 시작");
            Integer price = coffeeRepository.getPriceByName(name);
            future.complete(price);
        }).start();
        return future;
         */
    }

    @Override
    public CompletableFuture<Integer> getDiscountPriceAsync(Integer price) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("#getDiscountPriceAsync -> supplyAsync");
            return (int) (price * 0.9);
        }, threadPoolTaskExecutor);
    }
}
