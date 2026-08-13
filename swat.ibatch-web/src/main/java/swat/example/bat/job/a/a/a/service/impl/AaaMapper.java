package swat.example.bat.job.a.a.a.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import egovframework.example.bat.domain.trade.CustomerCredit;

@EgovMapper
public interface AaaMapper {

	int getAllCustomerCreditIds();

	List<CustomerCredit> getAllCustomerCredits(int _skiprows, int _pagesize);

	CustomerCredit getCustomerCreditById(int value);

	int updateCredit(CustomerCredit customerCredit);

}
