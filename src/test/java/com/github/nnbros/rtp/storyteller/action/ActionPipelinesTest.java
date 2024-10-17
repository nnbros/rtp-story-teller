package com.github.nnbros.rtp.storyteller.action;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.createTestActionContext;
import static org.mockito.Mockito.*;

public class ActionPipelinesTest {

	public static final String TEST_PIPELINE_RESULT = "testResult";

	@Test
	@SuppressWarnings("unchecked")
	void createFunctionConsumerActionPipeline() {
		ActionContext context = createTestActionContext();
		Function<ActionContext, String> actionProcessor = mock(Function.class);
		Consumer<String> nextActionHandler = mock(Consumer.class);
		when(actionProcessor.apply(context)).thenReturn(TEST_PIPELINE_RESULT);

		ActionPipeline pipeline = ActionPipelines.create(actionProcessor, nextActionHandler);
		pipeline.execute(context);

		verify(nextActionHandler).accept(TEST_PIPELINE_RESULT);
	}

	@Test
	@SuppressWarnings("unchecked")
	void createFunctionAndAltNextActionHandlerActionPipelineSuccessfulResult() {
		ActionContext context = createTestActionContext();
		Function<ActionContext, ActionResult<String>> actionProcessor = mock(Function.class);
		Consumer<String> nextActionHandler = mock(Consumer.class);
		Consumer<String> altNextActionHandler = mock(Consumer.class);
		ActionResult<String> successfulResult = new ActionResult<>(TEST_PIPELINE_RESULT, true);
		when(actionProcessor.apply(context)).thenReturn(successfulResult);

		ActionPipeline pipeline = ActionPipelines.create(actionProcessor, nextActionHandler, altNextActionHandler);
		pipeline.execute(context);

		verify(actionProcessor).apply(context);
		verify(nextActionHandler).accept(TEST_PIPELINE_RESULT);
		verifyNoInteractions(altNextActionHandler);
	}

	@Test
	@SuppressWarnings("unchecked")
	void createFunctionAndAltNextActionHandlerActionPipelineFailedResult() {
		ActionContext context = createTestActionContext();
		Function<ActionContext, ActionResult<String>> actionProcessor = mock(Function.class);
		Consumer<String> nextActionHandler = mock(Consumer.class);
		Consumer<String> altNextActionHandler = mock(Consumer.class);
		ActionResult<String> successfulResult = new ActionResult<>(TEST_PIPELINE_RESULT, false);
		when(actionProcessor.apply(context)).thenReturn(successfulResult);

		ActionPipeline pipeline = ActionPipelines.create(actionProcessor, nextActionHandler, altNextActionHandler);
		pipeline.execute(context);

		verify(actionProcessor).apply(context);
		verify(altNextActionHandler).accept(TEST_PIPELINE_RESULT);
		verifyNoInteractions(nextActionHandler);
	}

	@Test
	@SuppressWarnings("unchecked")
	void testCreate_withTwoConsumers() {
		ActionContext context = createTestActionContext();
		Consumer<ActionContext> actionProcessor = mock(Consumer.class);
		Consumer<ActionContext> nextActionHandler = mock(Consumer.class);

		ActionPipeline pipeline = ActionPipelines.create(actionProcessor, nextActionHandler);
		pipeline.execute(context);

		verify(actionProcessor).accept(context);
		verify(nextActionHandler).accept(context);
	}
}
