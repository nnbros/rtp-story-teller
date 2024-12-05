package com.github.nnbros.rtp.storyteller.action;

import com.github.nnbros.rtp.storyteller.exception.ActionNotFoundException;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerException;
import com.github.nnbros.rtp.storyteller.gateway.GatewayClient;
import com.github.nnbros.rtp.storyteller.telegram.UpdateType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.nnbros.rtp.storyteller.telegram.BotUtils.getMessageId;
import static com.github.nnbros.rtp.storyteller.telegram.BotUtils.getUserId;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActionService {
	private final List<ActionRegistrar> actionRegistrars;
	private final ThreadPoolTaskExecutor actionPipelineExecutor;
	private final GatewayClient gatewayClient;
	private final ActionErrorProcessor errorProcessor;
	private Map<String, ActionPipeline> actionPipelines;

	@PostConstruct
	void init() {
		log.trace("Starting to register action pipelines...");
		Map<String, ActionPipeline> tmpActionPipelines = new HashMap<>();
		actionRegistrars.forEach(actionRegistrar -> actionRegistrar.register(tmpActionPipelines));
		actionPipelines = Map.copyOf(tmpActionPipelines);
		log.trace("Action pipelines has been registered for actions: {}", actionPipelines.keySet());
	}

	public void process(String action, Update update, String data) throws StoryTellerException {

		Integer updateId = update.getUpdateId();
		log.info("New update for action [{}] has been received, update id is {}", action, updateId);
		log.trace("Update:\n{}", update);

		UpdateType updateType = UpdateType.getUpdateType(update);
		log.debug("Update type is [{}]", updateType);
		Long userId = getUserId(update, updateType);
		log.info("User id is [{}]", userId);

		try {
			Instant startProcessingTimestamp = Instant.now();
			Integer messageId = getMessageId(update, updateType);
			ActionContext actionContext = new ActionContext(action, userId, updateType, update, messageId, data);
			Optional.ofNullable(actionPipelines.get(action))
					.map(actionPipeline -> (Runnable) () -> actionPipeline.execute(actionContext))
					.map(actionPipelineExecutor::submitCompletable)
					.orElseThrow(() -> new ActionNotFoundException(action))
					.whenCompleteAsync((result, error) -> executePostActionProcessing(action, userId, error, startProcessingTimestamp), actionPipelineExecutor);
			log.info("The action has been successfully sent to the action pipeline");
		} catch (Exception e) {
			gatewayClient.releaseUserLock(userId);
			throw e;
		}
	}

	private void executePostActionProcessing(String action, long userId, Throwable error, Instant start) {
		try {
			if (error != null) {
				Throwable cause = error.getCause();
				log.warn("Error occurred during processing", cause);
				errorProcessor.process(action, userId, cause);
			}
		} catch (Exception e) {
			log.error("Failed to complete post-action steps, for action [{}]", action, e);
		} finally {
			Duration processingTime = Duration.between(start, Instant.now());
			log.info("Action [{}] has been processed for the user [{}], processing time: {}", action, userId, processingTime);
			gatewayClient.releaseUserLock(userId);
		}
	}
}
