package egovframework.example.bat.job;

import javax.sql.DataSource;

import org.egovframe.rte.bat.core.item.database.EgovJdbcBatchItemWriter;
import org.egovframe.rte.bat.core.item.database.support.EgovMethodMapItemPreparedStatementSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import egovframework.example.bat.domain.trade.CustomerCredit;
import egovframework.example.bat.domain.trade.CustomerCreditIncreaseProcessor;
import egovframework.example.bat.domain.trade.CustomerCreditRowMapper;

@Configuration
public class JdbcToJdbcJobConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcToJdbcJobConfig.class);
	
	@Autowired
	private org.springframework.batch.core.repository.JobRepository jobRepository;
	
    @Bean
    public Job jdbcToJdbcJob(Step jdbcToJdbcStep) {
        return new org.springframework.batch.core.job.builder.JobBuilder("jdbcToJdbcJob", jobRepository)
                .start(jdbcToJdbcStep)
                .build();
    }

    @Bean
    public Step jdbcToJdbcStep(org.springframework.batch.core.repository.JobRepository jobRepository, 
                               org.springframework.transaction.PlatformTransactionManager transactionManager) {
        return new org.springframework.batch.core.step.builder.StepBuilder("jdbcToJdbcStep", jobRepository)
                .<CustomerCredit,CustomerCredit>chunk(2, transactionManager)
                .reader(jdbcCursorItemReader(null))
                .processor(jdbcToJdbcItemProcessor())
                .writer(jdbcBatchItemWriter(null))
                .build();
    }
    
    
    @Bean
    @StepScope
    public JdbcCursorItemReader<CustomerCredit> jdbcCursorItemReader(DataSource dataSource) {
    	
        return new JdbcCursorItemReaderBuilder<CustomerCredit>()
        		.name("jdbcCursorItemReader")
        		.dataSource(dataSource)
        		.sql("select ID, NAME, CREDIT from CUSTOMER")
        		.verifyCursorPosition(true)
        		.rowMapper(rowMapper())
        		.build();
    }

    @Bean
    public CustomerCreditRowMapper rowMapper() {
    	return new CustomerCreditRowMapper();
    }

    @Bean
    public CustomerCreditIncreaseProcessor jdbcToJdbcItemProcessor() {
        return new CustomerCreditIncreaseProcessor();
    }

    @Bean
    @StepScope
    public EgovJdbcBatchItemWriter<CustomerCredit> jdbcBatchItemWriter(DataSource dataSource) {
    	
		EgovJdbcBatchItemWriter<CustomerCredit> jdbcBatchItemWriter	= new EgovJdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setAssertUpdates(true);
		jdbcBatchItemWriter.setItemPreparedStatementSetter(itemPreparedStatementSetter());
		jdbcBatchItemWriter.setSql("UPDATE CUSTOMER set credit =? where name =?");
		jdbcBatchItemWriter.setParams(new String[] {"credit","name"});
		jdbcBatchItemWriter.setDataSource(dataSource);
		
    	return jdbcBatchItemWriter;
    }
    
    @Bean
    public EgovMethodMapItemPreparedStatementSetter<CustomerCredit> itemPreparedStatementSetter() {
    	return new EgovMethodMapItemPreparedStatementSetter<>();
    }
    
}
