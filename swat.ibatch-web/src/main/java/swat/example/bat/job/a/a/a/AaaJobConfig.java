package swat.example.bat.job.a.a.a;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import egovframework.example.bat.domain.trade.CustomerCredit;
import lombok.extern.slf4j.Slf4j;
import swat.example.bat.job.a.a.a.service.impl.AaaMapper;

@Configuration
@Slf4j
public class AaaJobConfig {

	/**
	 * 증가할 수
	 */
	public static final BigDecimal FIXED_AMOUNT = new BigDecimal("5");

	@Bean
	Job aaaJob(JobRepository jobRepository, Step aaaStep) {
		return new JobBuilder("aaaJob", jobRepository).start(aaaStep).build();
	}

	@Bean
	Step aaaStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet aaaTasklet) {
		return new StepBuilder("aaaStep", jobRepository).tasklet(aaaTasklet, transactionManager).build();
	}

	@Bean
	Tasklet aaaTasklet(AaaMapper aaaMapper) {
		return (contribution, chunkContext) -> {

//			List<CustomerCredit> items = aaaMapper.getAllCustomerCredits(0, 10);
			List<CustomerCredit> items = aaaMapper.getAllCustomerCredits(0, Integer.MAX_VALUE);

			log.debug("size={}", items.size());

			for (CustomerCredit item : items) {
				if (log.isDebugEnabled()) {
					log.debug("getId={}", item.getId());
					log.debug("getName={}", item.getName());
					log.debug("getCredit={}", item.getCredit());
				}

				CustomerCredit customerCredit = item.increaseCreditBy(FIXED_AMOUNT);
				log.debug("getCredit={}", customerCredit.getCredit());

				int updateCreditResult = aaaMapper.updateCredit(customerCredit);
				log.debug("updateCreditResult={}", updateCreditResult);

				contribution.incrementReadCount();

				if (updateCreditResult > 0) {
					contribution.incrementWriteCount(updateCreditResult);
				}
			}

			return RepeatStatus.FINISHED;
		};
	}

}
