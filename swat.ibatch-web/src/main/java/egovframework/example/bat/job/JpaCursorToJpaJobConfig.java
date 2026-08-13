package egovframework.example.bat.job;

import java.util.HashMap;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;


import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import egovframework.example.bat.domain.jpa.Customer;
import egovframework.example.bat.domain.jpa.CustomerIncreaseProcessor;

@Configuration
public class JpaCursorToJpaJobConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JpaCursorToJpaJobConfig.class);
	
	@Autowired
	private org.springframework.batch.core.repository.JobRepository jobRepository;
	
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	
    @Bean
    public Job jpaCursorToJpaJob(Step jpaCursorToJpaStep) {
        return new org.springframework.batch.core.job.builder.JobBuilder("jpaCursorToJpaJob", jobRepository)
                .start(jpaCursorToJpaStep)
                .build();
    }

    @Bean
    public Step jpaCursorToJpaStep(JobRepository jobRepository, 
                                   @Qualifier("jpaTransactionManager") PlatformTransactionManager transactionManager) {
        return new StepBuilder("jpaCursorToJpaStep", jobRepository)
                .<Customer,Customer>chunk(2, transactionManager)
                .reader(jpaCursorItemReader())
                .processor(jpaItemProcessor())
                .writer(jpaCursorBatchItemWriter())
                //.writer(customItemWriter())
                .build();
    }
    
    
    @Bean
    @StepScope
    public JpaCursorItemReader<Customer> jpaCursorItemReader() {
    	
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", "%");
        
        return new JpaCursorItemReaderBuilder<Customer>()
        		.name("jpaCursorItemReader")
        		.queryString("select c from Customer c where id < 1000 and name like :name")
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(parameters)
//                .maxItemCount(10)
//                .currentItemCount(2)
                .build();
    }

    @Bean
    public CustomerIncreaseProcessor jpaItemProcessor() {
        return new CustomerIncreaseProcessor();
    }

    @Bean
    @StepScope
    public JpaItemWriter<Customer> jpaCursorBatchItemWriter() {
        
        return new JpaItemWriterBuilder<Customer>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();

    }
    
    @Bean
    public ItemWriter<Customer> jpaCursorCustomItemWriter() {
        return items -> {
            for (Customer item : items) {
                LOGGER.debug("===>>> "+item.getName());
            }
        };
    }
    
}
