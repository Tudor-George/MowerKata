package com.mowitnow.tondeuse.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.mowitnow.tondeuse.dto.helpers.MowerDataInputWarper;
import com.mowitnow.tondeuse.mapper.FileToDataInput;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
public class BatchConfig {

	private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final JobRepository jobRepository;
	
	@Value("${inputFilePath}")
    private String inputFilePath;
	@Autowired
	DataSourceConfig dataSourceConfig;

	@Autowired
	PlatformTransactionManager txManager;

	public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			JobRepository jobRepository) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.jobRepository = jobRepository;
	}

	@Bean(name = "mowitnowJob")
	public Job processFileJob() {
		return jobBuilderFactory.get("processFileJob")
				.incrementer(new RunIdIncrementer())
				.repository(getJobRepository())
				.flow(fileProcessingStep())
				.end()
				.build();
	}

	@Bean
	public Step fileProcessingStep() {
		return stepBuilderFactory.get("fileProcessingStep")
				.<MowerDataInputWarper, MowerDataInputWarper>chunk(1)
				.reader(itemReader())
				.processor(itemProcessor())
				.writer(itemWriter())
				.build();
	}

	
	@Bean
	public ItemReader<MowerDataInputWarper> itemReader() {
		return new FlatFileItemReaderBuilder<MowerDataInputWarper>()
				.name("mowerInputReader")
				.resource(new FileSystemResource(inputFilePath))
				.lineTokenizer(tokenizer())
				.fieldSetMapper(fieldSetMapper())
				.build();
	}

	@Bean
	public DefaultLineMapper<MowerDataInputWarper> lineMapper(LineTokenizer tokenizer,
			FieldSetMapper<MowerDataInputWarper> mapper) {
		var lineMapper = new DefaultLineMapper<MowerDataInputWarper>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(mapper);
		return lineMapper;
	}

	@Bean
	public BeanWrapperFieldSetMapper<MowerDataInputWarper> fieldSetMapper() {
		var fieldSetMapper = new BeanWrapperFieldSetMapper<MowerDataInputWarper>();
		fieldSetMapper.setTargetType(MowerDataInputWarper.class);
		return fieldSetMapper;
	}

	@Bean
	public DelimitedLineTokenizer tokenizer() {
		var tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter("\n");
		tokenizer.setNames("fileLine");
		tokenizer.setStrict(false);
		return tokenizer;
	}

	@Bean
	public ItemProcessor<MowerDataInputWarper, MowerDataInputWarper> itemProcessor() {

		return items -> FileToDataInput.process(items);
	}

	@Bean
	public ItemWriter<MowerDataInputWarper> itemWriter() {
		// Assuming the writer implementation, which could simply log the processed data
		return items -> {
			// Log the processed items
			for (MowerDataInputWarper item : items) {
				logger.info("Processed item: {}", item.getFileLine());
			}
		};
	}

	@Bean(name = "jobLauncher")
	public JobLauncher jobLauncher() throws Exception {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(getJobRepository());
		launcher.setTaskExecutor(new SyncTaskExecutor());
		return launcher;
	}

	public JobRepository getJobRepository() {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSourceConfig.dataSource());
		factory.setDatabaseType("H2");
		factory.setTransactionManager(txManager);
		try {
			return (JobRepository) factory.getObject();
		} catch (Exception e) {
			logger.error("Exception JobRepository Creation {}", e);
		}
		return null;

	}

}
