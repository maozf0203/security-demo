package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SsoApiGateWayJwt {

	public static void main(String[] args) {
		SpringApplication.run(SsoApiGateWayJwt.class, args);
	}

}