package com.github.nnbros.rtp.storyteller.api.controller;

import com.github.nnbros.rtp.storyteller.StorytellerMockMvcTest;
import com.github.nnbros.rtp.storyteller.configuration.StoryTellerProperties;
import com.github.nnbros.rtp.storyteller.registration.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Set;

import static com.github.nnbros.rtp.storyteller.api.controller.CacheController.REGISTRATION_URL_SUFFIX;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CacheController.class)
public class CacheControllerTest extends StorytellerMockMvcTest {
	public static final String TEST_BODY = "[1,2,3]";

	@MockBean
	private RegistrationService registrationService;

	@Test
	void clearCharacterRegistrationCache() throws Exception {
		Set<Long> userIds = Set.of(1L, 2L, 3L);

		StoryTellerProperties.Api api = properties.getApi();
		mockMvc.perform(delete(URL_TEMPLATE.formatted(api.getBaseUrl(), api.getCacheEndpointPrefix(), REGISTRATION_URL_SUFFIX))
						.contentType(CONTENT_TYPE)
						.content(TEST_BODY))
				.andExpect(status().isNoContent());

		verify(registrationService, times(1)).clearCharacterCache(userIds);
	}

	@Test
	void clearCharacterRegistrationCacheWithException() throws Exception {
		Set<Long> userIds = Set.of(1L, 2L, 3L);
		String testErrorMessage = "Test error";
		doThrow(new RuntimeException(testErrorMessage))
				.when(registrationService)
				.clearCharacterCache(userIds);

		StoryTellerProperties.Api api = properties.getApi();
		mockMvc.perform(delete(URL_TEMPLATE.formatted(api.getBaseUrl(), api.getCacheEndpointPrefix(), REGISTRATION_URL_SUFFIX))
						.contentType(CONTENT_TYPE)
						.content(TEST_BODY))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.error").value(testErrorMessage))
				.andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
				.andExpect(jsonPath("$.timestamp").exists());

		verify(registrationService, times(1)).clearCharacterCache(userIds);
	}
}
