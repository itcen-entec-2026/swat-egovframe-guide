/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package egovframework.example.bat.domain.jpa;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author 배치실행개발팀
 * @since 2012. 07.25
 * @version 1.0
 * @see
 *  <pre>
 *      개정이력(Modification Information)
 *
 *  수정일         수정자       수정내용
 *  ----------   --------   ---------------------------
 *  2023.03.31   신용호       최초 생성
 *  </pre>
 */
public class CustomerIncreaseProcessor implements ItemProcessor<Customer, Customer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerIncreaseProcessor.class);
	// 증가할 수
	public static final BigDecimal FIXED_AMOUNT = new BigDecimal("8");
	public static final long ID_INCREASE_STEP = 1000L;
	private Long jobId = 0L;
	private Long stepId = 0L;

	@BeforeStep
	public void getInterStepData(StepExecution stepExecution) {
		this.jobId = stepExecution.getJobExecution().getJobId();
	    this.stepId = stepExecution.getId();
	}

	/**
	 * FIXED_AMOUNT만큼 증가 시킨 후 return
	 */
	@Override
	public Customer process(Customer item) throws Exception {

		item.setCredit(item.getCredit().add(FIXED_AMOUNT).add(new BigDecimal(item.getId())));
		item.setId(item.getId()+(ID_INCREASE_STEP*(this.jobId+1)));
		LOGGER.debug("===>>> id = "+item.getId());
		LOGGER.debug("===>>> name = "+item.getName());
		LOGGER.debug("===>>> credit = "+item.getCredit());
		return item;
	}
}
