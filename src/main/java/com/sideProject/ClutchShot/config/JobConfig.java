package com.sideProject.ClutchShot.config;

import com.sideProject.ClutchShot.common.module.CsvReader;
import com.sideProject.ClutchShot.common.module.CsvScheduleWriter;
import com.sideProject.ClutchShot.dto.region.RegionCsvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final CsvReader csvReader;
    private final CsvScheduleWriter csvScheduleWriter;

    @Bean
    public Job regionDataLoadJob(JobRepository jobRepository, Step regionDataLoadStep) {
        return new JobBuilder("regionInformationLoadJob", jobRepository)
                .start(regionDataLoadStep)
                .build();
    }

    @Bean
    public Step regionDataLoadStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("regionDataLoadStep", jobRepository)
                .<RegionCsvDto, RegionCsvDto>chunk(100, platformTransactionManager)
                .reader(csvReader.csvScheduleReader())
                .writer(csvScheduleWriter)
                .build();
    }
}
