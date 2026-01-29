package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.common.action.ActionContext;
import com.github.nnbros.rtp.common.action.ActionResult;
import com.github.nnbros.rtp.storyteller.BotTestUtils;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_CLASS_1;
import static com.github.nnbros.rtp.storyteller.BotTestUtils.TEST_USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CharacterProcessorTest extends StorytellerTest {

	@Mock
	private CharacterService characterService;

	@InjectMocks
	private CharacterProcessor characterProcessor;

	@Test
	void updateActiveClass() {
		ActionContext testActionContext = BotTestUtils.createTestActionContext(TEST_CLASS_1);
		when(characterService.updateCharacterActiveClass(TEST_USER_ID, TEST_CLASS_1)).thenReturn(true);

		ActionResult<?> result = characterProcessor.updateActiveClass(testActionContext);

		assertTrue(result.isSuccessful());
		assertEquals(testActionContext, result.getActionContext());
		verify(characterService, times(1)).validateCharacter(testActionContext);
		verify(characterService, times(1)).updateCharacterActiveClass(TEST_USER_ID, TEST_CLASS_1);
	}

	@Test
	void updateActiveClassNoDataReceived() {
		ActionContext testActionContext = BotTestUtils.createTestActionContext(null);

		ActionResult<?> result = characterProcessor.updateActiveClass(testActionContext);

		assertTrue(result.isSuccessful());
		assertEquals(testActionContext, result.getActionContext());
		verify(characterService, times(1)).validateCharacter(testActionContext);
		verifyNoMoreInteractions(characterService);
	}

	@Test
	void updateActiveClassIfItWasAlreadyUpdated() {
		ActionContext testActionContext = BotTestUtils.createTestActionContext(TEST_CLASS_1);
		when(characterService.updateCharacterActiveClass(TEST_USER_ID, TEST_CLASS_1)).thenReturn(false);

		ActionResult<?> result = characterProcessor.updateActiveClass(testActionContext);

		assertFalse(result.isSuccessful());
		assertEquals(testActionContext, result.getActionContext());
		verify(characterService, times(1)).validateCharacter(testActionContext);
		verify(characterService, times(1)).updateCharacterActiveClass(TEST_USER_ID, TEST_CLASS_1);
	}
}
