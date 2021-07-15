package com.goldcrestwilma.completablefuture;

import java.util.concurrent.Future;

public interface CoffeeUseCase {

    int getPrice(String name);

    Future<Integer> getPriceAsync(String name);

    Future<Integer> getDiscountPriceAsync(Integer price);
}
