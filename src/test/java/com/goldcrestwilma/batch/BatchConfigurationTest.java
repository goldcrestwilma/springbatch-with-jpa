package com.goldcrestwilma.batch;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.goldcrestwilma.BatchApplication;
import com.goldcrestwilma.batch.config.BatchTestConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BatchApplication.class, BatchTestConfiguration.class})
@SpringBootTest
class BatchConfigurationTest {

    @Autowired
    private JobLauncherTestUtils testUtils;

    @Autowired
    private BatchConfiguration config;

    @Test
    @DisplayName("aaaa")
    void testEntireJob() throws Exception {
        final JobExecution result = testUtils.getJobLauncher()
            .run(config.job(), testUtils.getUniqueJobParameters());
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    void testSpecificStep() {
        assertThat(testUtils.launchStep("orderStep").getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

}
