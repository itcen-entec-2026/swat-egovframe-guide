package egovframework.example.bat.job;


import org.egovframe.rte.bat.core.item.database.EgovMyBatisPagingItemReader;
import org.egovframe.rte.bat.core.item.file.transform.EgovFieldExtractor;
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
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import egovframework.example.bat.domain.trade.CustomerCredit;
import egovframework.example.bat.domain.trade.CustomerCreditIncreaseProcessor;

@Configuration
public class MybatisToDelimitedJobConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisToDelimitedJobConfig.class);
	
	@Autowired
	private org.springframework.batch.core.repository.JobRepository jobRepository;
	
	
    @Bean
    public Job mybatisToDelimitedJob(Step mybatisToDelimitedStep) {
        return new org.springframework.batch.core.job.builder.JobBuilder("mybatisToDelimitedJob", jobRepository)
                .start(mybatisToDelimitedStep)
                .build();
    }

    @Bean
    public Step mybatisToDelimitedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("mybatisToDelimitedStep", jobRepository)
                .<CustomerCredit,CustomerCredit>chunk(2, transactionManager)
                .reader(mybatisToDelimitedPagingItemReader(null))
                .processor(mybatisToDelimitedItemProcessor())
                .writer(delimitedItemWriter(null,null))
                .build();
    }
    
    @Bean
    @StepScope
    public EgovMyBatisPagingItemReader<CustomerCredit> mybatisToDelimitedPagingItemReader(SqlSessionFactoryBean sqlSessionFactoryBean) {
        
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
    public CustomerCreditIncreaseProcessor mybatisToDelimitedItemProcessor() {
        return new CustomerCreditIncreaseProcessor();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<CustomerCredit> delimitedItemWriter(@Qualifier("delimitedToDelimitedJob.delimitedLineAggregator") DelimitedLineAggregator<CustomerCredit> lineAggregator
    		, @Value("#{jobParameters[outputFile]}") String outputFile) {
    	
    	FileSystemResource resource = new FileSystemResource(outputFile);
    	
    	return new FlatFileItemWriterBuilder<CustomerCredit>()
    			.name("delimitedItemWriter")
    			.resource(resource)
    			.lineAggregator(lineAggregator)
    			.saveState(false)
    			.build();
    }
    
    @Bean(name="delimitedToDelimitedJob.fieldExtractor")
    public EgovFieldExtractor<CustomerCredit> fieldExtractor() {
    	EgovFieldExtractor<CustomerCredit> fieldExtractor = new EgovFieldExtractor<>();
    	fieldExtractor.setNames(new String[] {"name","credit"});
    	return fieldExtractor;
    }
    
    @Bean(name="delimitedToDelimitedJob.delimitedLineAggregator")
    public DelimitedLineAggregator<CustomerCredit> lineAggregator(@Qualifier("delimitedToDelimitedJob.fieldExtractor") EgovFieldExtractor<CustomerCredit> fieldExtractor) {
    	DelimitedLineAggregator<CustomerCredit> lineAggregator = new DelimitedLineAggregator<>();
    	lineAggregator.setDelimiter(",");
    	lineAggregator.setFieldExtractor(fieldExtractor);
    	return lineAggregator;
    }
}
