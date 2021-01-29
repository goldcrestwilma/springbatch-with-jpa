package com.goldcrestwilma.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.goldcrestwilma.batch.common.StringWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfiguration {

    private static final String JOB_NAME = "DefaultJob";
    private static final String STEP_NAME = JOB_NAME + "Step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(step())
            .build();
    }

    @Bean
    public Step step() {
        return  stepBuilderFactory.get(STEP_NAME)
            .<String, StringWrapper>chunk(1)
            .reader(itemReader())
            .processor(itemProcess())
            .writer(itemWriter())
            .build();
    }


    private ItemReader<String> itemReader() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            list.add("count down " + i);
        }

        return new ListItemReader(list);
    }

    private ItemProcessor<String, StringWrapper> itemProcess() {
        return StringWrapper::new;
    }

    private ItemWriter<StringWrapper> itemWriter() {
        return System.out::println;
    }
}
