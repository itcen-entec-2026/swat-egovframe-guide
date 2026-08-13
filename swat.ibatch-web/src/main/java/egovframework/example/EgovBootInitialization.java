package egovframework.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EgovBootInitialization implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/batchList.do");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 정적 리소스 핸들러 설정 (WEB-INF 제외)
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/images/**").addResourceLocations("/images/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		// WEB-INF 경로는 뷰 리졸버가 처리하도록 설정
		registry.setOrder(org.springframework.core.Ordered.LOWEST_PRECEDENCE);
	}

}
