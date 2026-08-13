package egovframework.example.bat.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

import jakarta.annotation.PostConstruct;

@Configuration
public class EgovConfigDatasource {

	/**
	 * @Value 을 어노테이션을 이용하는 방법
	 */
	// @Value("${Globals.DbType}")
	// private String dbType;
	//
	// @Value("${Globals.DriverClassName}")
	// private String className;
	//
	// @Value("${Globals.Url}")
	// private String url;
	//
	// @Value("${Globals.UserName}")
	// private String userName;
	//
	// @Value("${Globals.Password}")
	// private String password;

	/**
	 * Environment 의존성 주입하여 사용하는 방법
	 */

	@Autowired
	Environment env;

	private String dbType;
	private String className;
	private String url;
	private String userName;
	private String password;

	@PostConstruct
	void init() {
		dbType = env.getProperty("Globals.DbType");
		// Exception 처리 필요
		className = env.getProperty("Globals." + dbType + ".DriverClassName");
		url = env.getProperty("Globals." + dbType + ".Url");
		userName = env.getProperty("Globals." + dbType + ".UserName");
		password = env.getProperty("Globals." + dbType + ".Password");
	}

	/**
	 * @return [dataSource 설정] HSQL 설정
	 */
	public DataSource dataSourceHSQL() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		builder.setType(EmbeddedDatabaseType.HSQL);
		builder.setName("batchdb");
		builder.addScript("classpath:/db/sampledb.script");

		// Spring Batch 5.x 스키마 자동 추가
		// 여러 경로 시도
		String[] schemaPaths = { "org/springframework/batch/core/schema-hsqldb.sql",
				"org/springframework/batch/core/schema-h2.sql" };

		for (String path : schemaPaths) {
			Resource schemaResource = new ClassPathResource(path);
			if (schemaResource.exists()) {
				builder.addScript("classpath:" + path);
				break;
			}
		}

		return builder.build();
	}

	/**
	 * @return [dataSource 설정] basicDataSource 설정
	 */
	private DataSource dataSourceBasic() {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(className);
		basicDataSource.setUrl(url);
		basicDataSource.setUsername(userName);
		basicDataSource.setPassword(password);
		return basicDataSource;
	}

	/**
	 * @return [DataSource 설정]
	 */
	@Bean(name = { "dataSource" })
	DataSource dataSource() {
		if ("hsql".equals(dbType)) {
			return dataSourceHSQL();
		} else {
			return dataSourceBasic();
		}
	}

	@Bean
	DefaultLobHandler lobHandler() {
		DefaultLobHandler lobHandler = new DefaultLobHandler();
		return lobHandler;
	}
}
