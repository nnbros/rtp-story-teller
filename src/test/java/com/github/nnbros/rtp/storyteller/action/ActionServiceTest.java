package com.github.nnbros.rtp.storyteller.action;

import com.github.nnbros.rtp.storyteller.StorytellerSpringBootTest;
import com.github.nnbros.rtp.storyteller.character.CharacterAction;
import com.github.nnbros.rtp.storyteller.exception.ActionNotFoundException;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerException;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.gateway.GatewayClient;
import com.github.nnbros.rtp.storyteller.registration.RegistrationService;
import com.github.nnbros.rtp.storyteller.registration.RegistrationTelegramClient;
import com.github.nnbros.rtp.storyteller.telegram.UpdateType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_USER_ID;
import static com.github.nnbros.rtp.storyteller.BotTestUtils.createTestCallbackQueryUpdate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ActionServiceTest extends StorytellerSpringBootTest {
	private static final int ACTIONS_COUNT = 6;
	private static final String TEST_ACTION_DATA = "testActionData";

	@MockBean
	private GatewayClient gatewayClient;
	@MockBean
	private RegistrationService registrationService;
	@MockBean
	private RegistrationTelegramClient registrationTelegramClient;
	@MockBean
	private ActionErrorProcessor errorProcessor;
	@SpyBean
	private ThreadPoolTaskExecutor actionPipelineExecutor;

	@Autowired
	private ActionService actionService;

	@Test
	public void initActionPipelines() {
		Object actionPipelines = ReflectionTestUtils.getField(actionService, "actionPipelines");
		if (!(actionPipelines instanceof Map<?, ?> testMap)) {
			throw new StoryTellerRuntimeException("Wrong actionPipelines type");
		}

		assertEquals(ACTIONS_COUNT, testMap.size());
	}

	@Test
	public void process() throws StoryTellerException {
		String actionName = CharacterAction.CREATE_START.getActionName();
		Update update = createTestCallbackQueryUpdate();

		actionService.process(actionName, update, TEST_ACTION_DATA);

		ActionContext actionContext = new ActionContext(actionName, TEST_USER_ID, UpdateType.CALLBACK_QUERY, update, TEST_ACTION_DATA);
		verify(registrationService, times(1)).createCharacter(actionContext);
		verify(registrationTelegramClient, times(1)).sendGenderOptions(actionContext);
		verify(actionPipelineExecutor, times(1)).submitCompletable(any(Runnable.class));
		verify(gatewayClient, times(1)).releaseUserLock(TEST_USER_ID);
	}

	@Test
	public void actionPipelineNotFound() {
		String actionName = "notExistedAction";
		Update update = createTestCallbackQueryUpdate();
		assertThrows(ActionNotFoundException.class, () -> actionService.process(actionName, update, TEST_ACTION_DATA));
		verify(gatewayClient, times(1)).releaseUserLock(TEST_USER_ID);
	}

	@Test
	public void processErrorThrownByActionPipeline() throws StoryTellerException, InterruptedException {
		String actionName = CharacterAction.CREATE_START.getActionName();
		Update update = createTestCallbackQueryUpdate();
		ActionContext actionContext = new ActionContext(actionName, TEST_USER_ID, UpdateType.CALLBACK_QUERY, update, TEST_ACTION_DATA);
		StoryTellerRuntimeException testException = new StoryTellerRuntimeException("Test exception");

		CountDownLatch countDownLatch = new CountDownLatch(1);
		doAnswer(invocation -> {
			countDownLatch.countDown();
			throw testException;
		}).when(registrationService).createCharacter(actionContext);

		actionService.process(actionName, update, TEST_ACTION_DATA);
		countDownLatch.await();

		verify(actionPipelineExecutor, times(1)).submitCompletable(any(Runnable.class));
		verify(errorProcessor, timeout(50L).times(1)).process(actionName, TEST_USER_ID, testException);
		verify(gatewayClient, times(1)).releaseUserLock(TEST_USER_ID);
	}
}
