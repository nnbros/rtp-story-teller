package com.github.nnbros.rtp.storyteller.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway",
		url = "${spring.cloud.openfeign.client.config.gateway.url}",
		path = "${spring.cloud.openfeign.client.config.gateway.path}")
public interface GatewayClient {

	@DeleteMapping("/users/{userId}/lock")
	void releaseUserLock(@PathVariable Long userId);
}
