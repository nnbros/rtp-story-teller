package com.github.nnbros.rtp.storyteller.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Setter
@Getter
@ConfigurationProperties(prefix = "storyteller")
public class StoryTellerProperties {
	@NotNull
	private Api api = new Api();
	@NotNull
	private RTPBot rtpBot = new RTPBot();
	@NotNull
	private ThreadPool actionProcessorThreadPool = new ThreadPool();
	@NotBlank
	private String characterNamePattern = "^[A-Za-zА-Яа-я0-9_]{4,32}$";

	@Setter
	@Getter
	public static class ThreadPool {
		@Positive
		private int min = 10;
		@Positive
		private int max = 30;
		@Positive
		private int queue = 10_000;
		private Duration awaitTerminationTimeout = Duration.ofMinutes(5);
		@NotBlank
		private String threadNamePrefix = "ActionProcessor-";
	}

	@Setter
	@Getter
	public static class Api {
		@URL
		private String baseUrl = "/api/v1";
		@URL
		private String actionsEndpointPrefix = "/actions";
	}

	@Setter
	@Getter
	public static class RTPBot {
		@NotBlank
		private String token;
	}
}
