package com.swiftstore;

import com.swiftstore.model.util.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class SwiftstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftstoreApplication.class, args);
	}
}
