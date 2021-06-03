package com.goldcrestwilma.step;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Writer implements ItemWriter<String> {

    private final Logger logger = LoggerFactory.getLogger(Writer.class);

    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String item : items) {
            logger.info("Writing the data: {} ", item);
        }
    }
}
