package egovframework.example.bat.job;

import org.egovframe.rte.bat.core.item.database.EgovMyBatisBatchItemWriter;
import org.egovframe.rte.bat.core.item.database.EgovMyBatisPagingItemReader;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;


import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import egovframework.example.bat.domain.trade.CustomerCredit;
import egovframework.example.bat.domain.trade.CustomerCreditIncreaseProcessor;

@Configuration
public class MybatisToMybatisJobConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisToMybatisJobConfig.class);
	
	@Autowired
	private org.springframework.batch.core.repository.JobRepository jobRepository;
	
	
    @Bean
    public Job myBatisToMybatisJob(Step myBatisToMybatisJobStep) {
        return new org.springframework.batch.core.job.builder.JobBuilder("myBatisToMybatisJob", jobRepository)
                .start(myBatisToMybatisJobStep)
                .build();
    }

    @Bean
    public Step myBatisToMybatisJobStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myBatisToMybatisStep", jobRepository)
                .<CustomerCredit,CustomerCredit>chunk(2, transactionManager)
                .reader(mybatisToMybatisPagingItemReader(null))
                .processor(mybatisToMybatisItemProcessor())
                .writer(myBatisBatchItemWriter(null))
                .build();
    }
    
    @Bean
    @StepScope
    public EgovMyBatisPagingItemReader<CustomerCredit> mybatisToMybatisPagingItemReader(SqlSessionFactoryBean sqlSessionFactoryBean) {
        
        EgovMyBatisPagingItemReader<CustomerCredit> myBatisPagingItemReader = new EgovMyBatisPagingItemReader<>();
        try {
			myBatisPagingItemReader.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	myBatisPagingItemReader.setQueryId("Customer.getAllCustomerCredits");
    	myBatisPagingItemReader.setPageSize(10);
    	
        return myBatisPagingItemReader;
    }
    
    @Bean
    public CustomerCreditIncreaseProcessor mybatisToMybatisItemProcessor() {
        return new CustomerCreditIncreaseProcessor();
    }

    @Bean
    @StepScope
    public EgovMyBatisBatchItemWriter<CustomerCredit> myBatisBatchItemWriter(SqlSessionFactoryBean sqlSessionFactoryBean) {
    	
    	EgovMyBatisBatchItemWriter<CustomerCredit> myBatisBatchItemWriter	= new EgovMyBatisBatchItemWriter<>();
    	try {
			myBatisBatchItemWriter.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	myBatisBatchItemWriter.setStatementId("Customer.updateCredit");
		
    	return myBatisBatchItemWriter;
    }
    
}
