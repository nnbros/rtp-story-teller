package com.github.nnbros.rtp.storyteller.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
@EnableConfigurationProperties({StoryTellerProperties.class, Localization.class})
public class MainConfiguration {

	@Bean(name = "actionPipelineExecutor")
	public ThreadPoolTaskExecutor actionPipelineExecutor(StoryTellerProperties storyTellerProperties) {
		StoryTellerProperties.ThreadPool actionProcessor = storyTellerProperties.getActionProcessorThreadPool();
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutorBuilder().corePoolSize(actionProcessor.getMin())
				.maxPoolSize(actionProcessor.getMax())
				.threadNamePrefix(actionProcessor.getThreadNamePrefix())
				.awaitTermination(true)
				.awaitTerminationPeriod(actionProcessor.getAwaitTerminationTimeout())
				.build();
		executor.initialize();
		return executor;
	}
}
