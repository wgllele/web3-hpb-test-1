package io.hpb.web3;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import io.hpb.web3.common.SpringBootContext;


@SpringBootApplication
@Configuration
@EnableScheduling
@EnableAsync
@EnableWebSecurity
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = "io.hpb.web3.")
public class Web3HpbTestApplication {

	public static void main(String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication springApplication = new SpringApplication(Web3HpbTestApplication.class);
		springApplication.setAddCommandLineProperties(false);
		springApplication.setBannerMode(Banner.Mode.OFF);
		ApplicationContext aplicationContext = springApplication.run(args);
		SpringBootContext.setAplicationContext(aplicationContext);
	}
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			if (SpringBootContext.getAplicationContext() == null) {
				SpringBootContext.setAplicationContext(ctx);
			}
		};
	}
}
