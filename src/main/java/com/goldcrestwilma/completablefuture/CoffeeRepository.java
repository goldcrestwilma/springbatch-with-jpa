package com.goldcrestwilma.completablefuture;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

@Repository
public class CoffeeRepository {

    private final Map<String, Coffee> coffees = Maps.newHashMap();

    @PostConstruct
    public void init() {
        coffees.put("latte", Coffee.builder().name("latte").price(1000).build());
        coffees.put("mocha", Coffee.builder().name("moca").price(1500).build());
        coffees.put("americano", Coffee.builder().name("americano").price(4000).build());
    }

    public int getPriceByName(String name) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return coffees.get(name).getPrice();
    }
}
