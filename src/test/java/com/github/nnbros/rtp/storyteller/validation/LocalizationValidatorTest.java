package com.github.nnbros.rtp.storyteller.validation;

import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.character.*;
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
	@Mock
	private ArmyService armyService;
	@Mock
	private SkillService skillService;
	@Spy
	private Localization localization = createTestLocalization();

	@InjectMocks
	private LocalizationValidator validator;

	@BeforeEach
	public void provideTestData() {
		when(classService.getAllClasses()).thenReturn(
				Set.of(
						TEST_CLASS_POJO_1,
						TEST_CLASS_POJO_2,
						TEST_CLASS_POJO_3)
		);
		when(skillService.getAllSkills()).thenReturn(
				Set.of(
						TEST_SKILL_POJO_1,
						TEST_SKILL_POJO_2,
						TEST_SKILL_POJO_3)
		);
		when(armyService.getAllArmies()).thenReturn(
				Set.of(
						TEST_ARMY_POJO_1,
						TEST_ARMY_POJO_2,
						TEST_ARMY_POJO_3)
		);
	}

	@Test
	public void validateLocalizationConsistency() {
		validator.run(null);

		verify(classService, times(1)).getAllClasses();
		verify(skillService, times(1)).getAllSkills();
		verify(armyService, times(1)).getAllArmies();
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

	@Test
	public void failArmiesValidation() {
		when(localization.getArmies()).thenReturn(Collections.emptyMap());

		assertThrows(IllegalStateException.class, () -> validator.run(null));
	}
}
