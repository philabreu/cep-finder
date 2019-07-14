package com.srmasset.thcepdetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableHystrix
@SpringBootApplication
public class ThCepDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThCepDetailsApplication.class, args);
	}

}
