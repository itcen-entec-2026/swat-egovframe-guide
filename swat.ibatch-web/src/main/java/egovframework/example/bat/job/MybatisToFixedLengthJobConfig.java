package egovframework.example.bat.job;

import org.egovframe.rte.bat.core.item.database.EgovMyBatisPagingItemReader;
import org.egovframe.rte.bat.core.item.file.transform.EgovFieldExtractor;
import org.egovframe.rte.bat.core.item.file.transform.EgovFixedLengthLineAggregator;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import egovframework.example.bat.domain.trade.CustomerCredit;
import egovframework.example.bat.domain.trade.CustomerCreditIncreaseProcessor;

@Configuration
public class MybatisToFixedLengthJobConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisToFixedLengthJobConfig.class);
	
	@Autowired
	private org.springframework.batch.core.repository.JobRepository jobRepository;
	
	
    @Bean
    public Job mybatisToFixedLengthJob(Step mybatisToFixedLengthStep) {
        return new org.springframework.batch.core.job.builder.JobBuilder("mybatisToFixedLengthJob", jobRepository)
                .start(mybatisToFixedLengthStep)
                .build();
    }

    @Bean
    public Step mybatisToFixedLengthStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("mybatisToFixedLengthStep", jobRepository)
                .<CustomerCredit,CustomerCredit>chunk(2, transactionManager)
                .reader(mybatisToFixedLengthPagingItemReader(null))
                .processor(mybatisToFixedLengthItemProcessor())
                .writer(fixedLengthItemWriter(null))
                .build();
    }
    
    @Bean
    @StepScope
    public EgovMyBatisPagingItemReader<CustomerCredit> mybatisToFixedLengthPagingItemReader(SqlSessionFactoryBean sqlSessionFactoryBean) {
        
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
    public CustomerCreditIncreaseProcessor mybatisToFixedLengthItemProcessor() {
        return new CustomerCreditIncreaseProcessor();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<CustomerCredit> fixedLengthItemWriter(@Value("#{jobParameters[outputFile]}") String outputFile) {
    	
    	LOGGER.debug("===>>> outputFile = "+outputFile);
    	
    	FileSystemResource resource = new FileSystemResource(outputFile);
    	
    	return new FlatFileItemWriterBuilder<CustomerCredit>()
    			.name("fixedLengthItemWriter")
    			.resource(resource)
    			.lineAggregator(lineAggregator())
    			.saveState(false)
    			.build();
    }
    
    @Bean
    public EgovFieldExtractor<CustomerCredit> fieldExtractor() {
    	EgovFieldExtractor<CustomerCredit> fieldExtractor = new EgovFieldExtractor<>();
    	fieldExtractor.setNames(new String[] {"name","credit"});
    	return fieldExtractor;
    }
    
    @Bean
    public EgovFixedLengthLineAggregator<CustomerCredit> lineAggregator() {
    	EgovFixedLengthLineAggregator<CustomerCredit> lineAggregator = new EgovFixedLengthLineAggregator<>();
    	lineAggregator.setFieldRanges(new int[] {9,9});
    	lineAggregator.setFieldExtractor(fieldExtractor());
    	return lineAggregator;
    }
}
