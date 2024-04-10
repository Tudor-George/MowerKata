package com.mowitnow.tondeuse.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import com.mowitnow.tondeuse.dto.helpers.GridMowerDataInput;
import com.mowitnow.tondeuse.io.FileLoader;
import com.mowitnow.tondeuse.mapper.FileToDataInput;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final Logger logger = LoggerFactory.getLogger(FileToDataInput.class);
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job processFileJob() {
        return jobBuilderFactory.get("processFileJob")
                .incrementer(new RunIdIncrementer())
                .flow(fileProcessingStep())
                .end()
                .build();
    }

    @Bean
    public Step fileProcessingStep() {
    	  return stepBuilderFactory.get("fileProcessingStep")
                  .<List<String>, GridMowerDataInput>chunk(1) // Assuming you're processing the whole file as one item
                  .processor(itemProcessor())
                  .writer(itemWriter())
                  .build();
    }

  
    @Bean
    public ItemProcessor<List<String>, GridMowerDataInput> itemProcessor() {
        return items -> FileToDataInput.transformFileInput(items);
    }
    
    
    @Bean
    public ItemWriter<GridMowerDataInput> itemWriter() {
        // Assuming your writer implementation, which could simply log the processed data or store it
        return items -> {
            // Log or save the processed items
            for (GridMowerDataInput item : items) {
                logger.info("Processed item: {}", item);
            }
        };
    }

   
}
