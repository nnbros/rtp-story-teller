package com.github.nnbros.rtp.storyteller.telegram.ui;

import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Map;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuTelegramClientTest.TEST_CHARACTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ParameterServiceTest extends StorytellerTest {

	@Mock
	private CharacterService characterService;
	@Spy
	private final Localization localization = createTestLocalization();

	@InjectMocks
	private ParameterService parameterService;

	@Test
	public void buildCharacterBaseParameters() {
		ActionContext testActionContext = createTestActionContext();
		Parameter additionalParameter = Parameter.of("testParam1", "testValue1");
		Map<String, String> testParameters = createTestCharacterBaseParameters(additionalParameter);
		when(characterService.getByUserId(TEST_USER_ID)).thenReturn(createTestCharacter());

		Map<String, String> parameters = parameterService.buildCharacterBaseParameters(testActionContext, TEST_CHARACTER, additionalParameter);

		assertEquals(testParameters, parameters);
	}
}
