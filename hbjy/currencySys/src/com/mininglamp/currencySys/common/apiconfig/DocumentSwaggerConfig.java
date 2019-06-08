package com.mininglamp.currencySys.common.apiconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@Configuration
@EnableWebMvc
@EnableSwagger
public class DocumentSwaggerConfig{
	
	private SpringSwaggerConfig springSwaggerConfig;
	
	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}
	/**
	 * Every SwaggerSpringMVCPlugin bean is picked up by the swagger-mvc framework - allowing for
	 * multiple swagger groups i.e.same code base multiple swager resource listings
	 * @return
	 */
	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {
		SwaggerSpringMvcPlugin mvcPlugin = new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(".*?");
		return mvcPlugin;
	}
	

	private ApiInfo apiInfo(){
		ApiInfo apiInfo = new ApiInfo(
							"通用接口服务", 
							"接口开发人员应详细描述接口的信息，包括入参格式、可选值等", 
							"[API terms of service]",
							"[API Contact Email]", 
							"[API Licence Type]", 
							"[API Licence URL]");
		return apiInfo;
	}
	
}
