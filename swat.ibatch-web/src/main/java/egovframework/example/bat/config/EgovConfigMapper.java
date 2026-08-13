package egovframework.example.bat.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = { "egovframework", "swat" })
public class EgovConfigMapper {

	@Bean(name = "egov.sqlSession")
	SqlSessionFactoryBean sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws IOException {
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean
				.setConfigLocation(pmrpr.getResource("classpath:/egovframework/mapper/config/mapper-config.xml"));
//		sqlSessionFactoryBean.setMapperLocations(
//				pmrpr.getResource("classpath:/egovframework/mapper/example/bat/Egov_Example_SQL.xml"),
//				pmrpr.getResource("classpath:/swat/mapper/example/**/.*.xml"));

		Resource[] egovResources = pmrpr
				.getResources("classpath:/egovframework/mapper/example/bat/Egov_Example_SQL.xml");

		Resource[] swatResources = pmrpr.getResources("classpath*:/swat/mapper/example/**/*.xml");

		Resource[] mapperLocations = Stream.concat(Arrays.stream(egovResources), Arrays.stream(swatResources))
				.toArray(Resource[]::new);

		sqlSessionFactoryBean.setMapperLocations(mapperLocations);

		/*
		 * classpath:/egovframework/mapper/example/bat/Egov_Example_SQL_altibase.xml
		 * classpath:/egovframework/mapper/example/bat/Egov_Example_SQL_cubrid.xml
		 * classpath:/egovframework/mapper/example/bat/Egov_Example_SQL_mysql.xml
		 * classpath:/egovframework/mapper/example/bat/Egov_Example_SQL_oracle.xml
		 * classpath:/egovframework/mapper/example/bat/Egov_Example_SQL_tibero.xml
		 */
		return sqlSessionFactoryBean;
	}

	@Bean
	SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
