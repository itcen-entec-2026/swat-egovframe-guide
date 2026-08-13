package egovframework.example.bat.job;

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
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import egovframework.example.bat.domain.jpa.Customer;
import egovframework.example.bat.domain.jpa.Customer2;
import egovframework.example.bat.domain.jpa.CustomerIncreaseProcessor2;

@Configuration
public class JpaPagingToJpaJobConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JpaPagingToJpaJobConfig.class);
	
	@Autowired
	private org.springframework.batch.core.repository.JobRepository jobRepository;
	
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	
    @Bean
    public Job jpaPagingToJpaJob(Step jpaPagingToJpaStep) {
        return new org.springframework.batch.core.job.builder.JobBuilder("jpaPagingToJpaJob", jobRepository)
                .start(jpaPagingToJpaStep)
                .build();
    }

    @Bean
    public Step jpaPagingToJpaStep(JobRepository jobRepository, 
                                   @Qualifier("jpaTransactionManager") PlatformTransactionManager transactionManager) {
        return new StepBuilder("jpaPagingToJpaStep", jobRepository)
                .<Customer,Customer2>chunk(2, transactionManager)
                .reader(jpaPagingItemReader())
                .processor(jpaItemProcessor2())
                .writer(jpaPagingBatchItemWriter())
                //.writer(customItemWriter())
                .build();
    }
    
    
    @Bean
    @StepScope
    public JpaPagingItemReader<Customer> jpaPagingItemReader() {
    	
        return new JpaPagingItemReaderBuilder<Customer>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(2)
                .queryString("select c from Customer c")
                .build();
    }

    @Bean
    public CustomerIncreaseProcessor2 jpaItemProcessor2() {
        return new CustomerIncreaseProcessor2();
    }
    
    @Bean
    @StepScope
    public JpaItemWriter<Customer2> jpaPagingBatchItemWriter() {
        
        return new JpaItemWriterBuilder<Customer2>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();

    }
    
    @Bean
    public ItemWriter<Customer> jpaPagingCustomItemWriter() {
        return items -> {
            for (Customer item : items) {
                LOGGER.debug("===>>> "+item.getName());
            }
        };
    }
    
}
