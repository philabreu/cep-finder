package com.srmasset.thcepdetails.endpoint;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckEndpoint implements HealthIndicator {

	@Override
	public Health health() {
		int errorCode = check(); 
		if (errorCode != 1) {
			return Health.down().withDetail("Error Code", errorCode).build();
		}
		return Health.up().build();

	}

	public int check() {
		return 1;
	}

}
