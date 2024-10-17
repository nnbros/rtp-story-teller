package com.github.nnbros.rtp.storyteller.api.controller;

import com.github.nnbros.rtp.storyteller.StorytellerMockMvcTest;
import com.github.nnbros.rtp.storyteller.action.ActionService;
import com.github.nnbros.rtp.storyteller.configuration.StoryTellerProperties;
import com.github.nnbros.rtp.storyteller.exception.ActionNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ActionController.class)
class ActionControllerTest extends StorytellerMockMvcTest {
	public static final String TEST_UPDATE_BODY = "{\"update_id\": %s}".formatted(TEST_UPDATE_ID);
	public static final String DATA_PARAM = "data";

	@MockBean
	private ActionService actionService;

	@Test
	void processAction() throws Exception {
		Update update = createTestEmptyUpdate();

		StoryTellerProperties.Api api = properties.getApi();
		mockMvc.perform(post(URL_TEMPLATE.formatted(api.getBaseUrl(), api.getActionsEndpointPrefix(), TEST_ACTION_NAME))
						.contentType(CONTENT_TYPE)
						.content(TEST_UPDATE_BODY)
						.param(DATA_PARAM, TEST_ACTION_DATA))
				.andExpect(status().isNoContent());

		verify(actionService, times(1)).process(TEST_ACTION_NAME, update, TEST_ACTION_DATA);
	}

	@Test
	void processActionWithStorytellerException() throws Exception {
		Update update = createTestEmptyUpdate();
		String testErrorMessage = "Action with id [%s] was not found".formatted(TEST_ACTION_NAME);
		doThrow(new ActionNotFoundException(TEST_ACTION_NAME))
				.when(actionService)
				.process(TEST_ACTION_NAME, update, TEST_ACTION_DATA);

		StoryTellerProperties.Api api = properties.getApi();
		mockMvc.perform(post(URL_TEMPLATE.formatted(api.getBaseUrl(), api.getActionsEndpointPrefix(), TEST_ACTION_NAME))
						.contentType(CONTENT_TYPE)
						.content(TEST_UPDATE_BODY)
						.param(DATA_PARAM, TEST_ACTION_DATA))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value(testErrorMessage))
				.andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
				.andExpect(jsonPath("$.timestamp").exists());

		verify(actionService, times(1)).process(TEST_ACTION_NAME, update, TEST_ACTION_DATA);
	}

	@Test
	void processActionWithInternalServerError() throws Exception {
		Update update = createTestEmptyUpdate();
		String testErrorMessage = "Test error message";
		doThrow(new RuntimeException(testErrorMessage))
				.when(actionService)
				.process(TEST_ACTION_NAME, update, TEST_ACTION_DATA);

		StoryTellerProperties.Api api = properties.getApi();
		mockMvc.perform(post(URL_TEMPLATE.formatted(api.getBaseUrl(), api.getActionsEndpointPrefix(), TEST_ACTION_NAME))
						.contentType(CONTENT_TYPE)
						.content(TEST_UPDATE_BODY)
						.param(DATA_PARAM, TEST_ACTION_DATA))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.error").value(testErrorMessage))
				.andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
				.andExpect(jsonPath("$.timestamp").exists());

		verify(actionService, times(1)).process(TEST_ACTION_NAME, update, TEST_ACTION_DATA);
	}
}
