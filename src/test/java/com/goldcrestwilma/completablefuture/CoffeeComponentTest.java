package com.goldcrestwilma.completablefuture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CoffeeRepository.class, CoffeeComponent.class, TaskConfig.class})
class CoffeeComponentTest {

    private final Logger logger = LoggerFactory.getLogger(CoffeeComponentTest.class);

    @Autowired
    private CoffeeComponent coffeeComponent;

    @Test
    void testSyncBlockingCall() {
        int expectedPrice = 1000;
        int resultPrice = coffeeComponent.getPrice("latte");
        System.out.println("전달받음");

        assertThat(expectedPrice).isEqualTo(resultPrice);
    }

    @Test
    void testAsyncCall() {
        int expectedPrice = 1000;
        CompletableFuture<Integer> future = coffeeComponent.getPriceAsync("latte");
        System.out.println("아직 전달 받기 전, 다른 작업 수행 가능");
        int resultPrice = future.join();
        System.out.println("전달받음");

        assertThat(expectedPrice).isEqualTo(resultPrice);
    }

    @Test
    void testAsyncNonBlockingThenAccept() {
        int expectedPrice = 1000;

        CompletableFuture<Void> future =
            coffeeComponent.getPriceAsync("latte")
                           .thenAccept(price -> {
                               logger.info("콜백, 가격: " + price);
                               assertThat(expectedPrice).isEqualTo(price);
                           });
        logger.info("아직 전달 받기 전, 다른 작업 수행 가능");
        assertThat(future.join()).isNull();
    }

    @Test
    void testAsyncNonBlockingThenApply() {
        int expectedPrice = 1100;

        CompletableFuture<Void> future =
            coffeeComponent.getPriceAsync("latte")
                           .thenApply(price -> {
                               logger.info("같은 쓰레드로 동작");
                               return price + 100;
                           })
                           .thenAccept(price -> {
                               logger.info("콜백, 가격: " + price);
                               assertThat(expectedPrice).isEqualTo(price);
                           });
        logger.info("아직 전달 받기 전, 다른 작업 수행 가능");
        assertThat(future.join()).isNull();
    }

    @Test
    void testAsyncNonBlockingThenApplyEachOtherThread() {
        int expectedPrice = 1100;
        Executor executor = Executors.newFixedThreadPool(5);

        CompletableFuture<Void> future =
            coffeeComponent.getPriceAsync("latte")
                           .thenApplyAsync(price -> {
                               logger.info("다른 쓰레드로 동작");
                               return price + 100;
                           }, executor)
                           .thenAcceptAsync(price -> {
                               logger.info("콜백, 가격: " + price);
                               assertThat(expectedPrice).isEqualTo(price);
                           }, executor);
        logger.info("아직 전달 받기 전, 다른 작업 수행 가능");
        assertThat(future.join()).isNull();
    }

    @Test
    void testThenCombine() {
        // given
        int expectedPrice = 5000;
        CompletableFuture<Integer> future1 = coffeeComponent.getPriceAsync("latte");
        CompletableFuture<Integer> future2 = coffeeComponent.getPriceAsync("americano");

        logger.info("각 쓰레드에 지연시간 1초가 존재 -> 소요시간 2초 예상");

        // when
        Integer actualPrice = future1.thenCombine(future2, Integer::sum).join();

        // then
        assertThat(expectedPrice).isEqualTo(actualPrice);
        logger.info("별도의 쓰레드에서 수행되기 때문에 1초 소요");
    }

    @Test
    void testThenCompose() {
        // given
        int expectedPrice = 900;
        CompletableFuture<Integer> future = coffeeComponent.getPriceAsync("latte");

        // when
        Integer actualPrice = future.thenCompose(result -> coffeeComponent.getDiscountPriceAsync(result)).join();

        // then
        assertThat(expectedPrice).isEqualTo(actualPrice);
    }

    @Test
    void testAllOf() {
        // given
        int expectedPrice = 6500;
        CompletableFuture<Integer> future1 = coffeeComponent.getPriceAsync("latte");
        CompletableFuture<Integer> future2 = coffeeComponent.getPriceAsync("mocha");
        CompletableFuture<Integer> future3 = coffeeComponent.getPriceAsync("americano");

        // when
        List<CompletableFuture<Integer>> futureList = List.of(future1, future2, future3);
        Integer actualPrice = CompletableFuture.allOf(future1, future2, future3)
                                               .thenApply(
                                                   Void -> futureList.stream()
                                                                     .map(CompletableFuture::join)
                                                                     .collect(Collectors.toList()))
                                               .join()
                                               .stream()
                                               .reduce(0, Integer::sum);

        // then
        assertThat(expectedPrice).isEqualTo(actualPrice);
    }
}
