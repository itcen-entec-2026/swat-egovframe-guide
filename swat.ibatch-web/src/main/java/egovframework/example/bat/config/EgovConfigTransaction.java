package egovframework.example.bat.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @ClassName : EgovConfigAppTransaction.java
 * @Description : Transaction 설정
 *
 * @author : 윤주호
 * @since : 2021. 7. 20
 * @version : 1.0
 *
 *          <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 *          </pre>
 *
 */
@Configuration
public class EgovConfigTransaction {

	@Autowired
	DataSource dataSource;

	/**
	 * JDBC/MyBatis 작업용 DataSourceTransactionManager JPA 작업은
	 * EgovConfigEntityManager의 jpaTransactionManager 사용
	 */
	@Bean(name = "transactionManager")
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}

}
