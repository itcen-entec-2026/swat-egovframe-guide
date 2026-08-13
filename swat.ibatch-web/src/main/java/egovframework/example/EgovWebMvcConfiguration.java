package egovframework.example;

import java.util.Properties;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

@Configuration
public class EgovWebMvcConfiguration {

	@Bean
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		Properties prop = new Properties();
		prop.setProperty("org.springframework.dao.DataAccessException", "cmmn/dataAccessFailure");
		prop.setProperty("org.springframework.transaction.TransactionException", "cmmn/transactionFailure");
		prop.setProperty("org.egovframe.rte.fdl.cmmn.exception.EgovBizException", "cmmn/egovError");
		prop.setProperty("org.springframework.security.AccessDeniedException", "cmmn/egovError");
		prop.setProperty("java.lang.Throwable", "cmmn/bizError");

		Properties statusCode = new Properties();
		statusCode.setProperty("/common/error", "400");
		statusCode.setProperty("/common/error", "500");

		SimpleMappingExceptionResolver smer = new SimpleMappingExceptionResolver();
		smer.setDefaultErrorView("cmmn/bizError");
		smer.setExceptionMappings(prop);
		smer.setStatusCodes(statusCode);
		smer.setDefaultStatusCode(500);
		return smer;
	}


	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> encodingFilterBean() {
		FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setForceEncoding(true);
		filter.setEncoding("UTF-8");
		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns("*.do");
		return registrationBean;
	}


}
