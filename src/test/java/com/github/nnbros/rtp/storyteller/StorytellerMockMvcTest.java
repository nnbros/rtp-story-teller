package com.github.nnbros.rtp.storyteller;

import com.github.nnbros.rtp.storyteller.configuration.StoryTellerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class StorytellerMockMvcTest {
	public static final String CONTENT_TYPE = "application/json";
	public static final String URL_TEMPLATE = "%s%s/%s";

	protected final StoryTellerProperties properties = new StoryTellerProperties();

	@Autowired
	protected MockMvc mockMvc;
}
