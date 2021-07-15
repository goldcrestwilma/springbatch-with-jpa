package com.goldcrestwilma.completablefuture;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Coffee {

    private final String name;
    private final int price;
}
