package com.github.nnbros.rtp.storyteller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public abstract class StorytellerTest {
	private AutoCloseable closeableMockContext;

	@BeforeEach
	public void openMocks() {
		closeableMockContext = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	public void releaseMocks() throws Exception {
		closeableMockContext.close();
	}
}
