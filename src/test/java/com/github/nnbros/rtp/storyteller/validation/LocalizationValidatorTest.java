package com.github.nnbros.rtp.storyteller.validation;

import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.character.ClassService;
import com.github.nnbros.rtp.storyteller.character.SkillDictionary;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Set;

import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LocalizationValidatorTest extends StorytellerTest {

	@Mock
	private ClassService classService;
	@Spy
	private Localization localization = createTestLocalization();

	@InjectMocks
	private LocalizationValidator validator;

	@BeforeEach
	public void provideTestData() {
		when(classService.getAllClasses()).thenReturn(
				Set.of(
						createTestClassDictionary(TEST_CLASS_1),
						createTestClassDictionary(TEST_CLASS_2),
						createTestClassDictionary(TEST_CLASS_3))
		);
		when(classService.getAllSkills()).thenReturn(
				Set.of(
						new SkillDictionary(TEST_SKILL_1),
						new SkillDictionary(TEST_SKILL_2),
						new SkillDictionary(TEST_SKILL_3))
		);
	}

	@Test
	public void validateLocalizationConsistency() {
		validator.run(null);

		verify(classService, times(1)).getAllClasses();
		verify(classService, times(1)).getAllSkills();
	}

	@Test
	public void failClassesValidation() {
		when(localization.getClasses()).thenReturn(Collections.emptyMap());

		assertThrows(IllegalStateException.class, () -> validator.run(null));
	}

	@Test
	public void failSkillsValidation() {
		when(localization.getSkills()).thenReturn(Collections.emptyMap());

		assertThrows(IllegalStateException.class, () -> validator.run(null));
	}
}
