package com.github.nnbros.rtp.storyteller.validation;

import com.github.nnbros.rtp.storyteller.character.*;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class LocalizationValidator implements ApplicationRunner {
	public static final String LOCALIZATION_ERROR_MSG_TEMPLATE = "Localization %s data mismatch between DB and localization files!\nDB: %s\nLocalization file: %s";
	public static final String CLASSES = "classes";
	public static final String ARMIES = "armies";
	public static final String SKILLS = "skills";

	private final ClassService classService;
	private final SkillService skillService;
	private final ArmyService armyService;
	private final Localization localization;

	@Override
	public void run(ApplicationArguments args) {
		log.debug("Starting to validate localization consistency...");
		validateClasses();
		validateArmies();
		validateSkills();
		log.debug("Localization consistency check passed");
	}

	private void validateClasses() {
		log.debug("Validating class consistency");
		Set<String> classes = classService.getAllClasses()
				.stream()
				.map(ClassDictionary::name)
				.collect(Collectors.toSet());

		HashSet<String> localizedClasses = new HashSet<>(localization.getClasses().keySet());
		localizedClasses.retainAll(classes);
		if (localizedClasses.size() != classes.size()) {
			throw new IllegalStateException(LOCALIZATION_ERROR_MSG_TEMPLATE.formatted(CLASSES, classes, localizedClasses));
		}
	}

	private void validateArmies() {
		log.debug("Validating army consistency");
		Set<String> armies = armyService.getAllArmies()
				.stream()
				.map(ArmyDictionary::name)
				.collect(Collectors.toSet());

		HashSet<String> localizedArmies = new HashSet<>(localization.getArmies().keySet());
		localizedArmies.retainAll(armies);
		if (localizedArmies.size() != armies.size()) {
			throw new IllegalStateException(LOCALIZATION_ERROR_MSG_TEMPLATE.formatted(ARMIES, armies, localizedArmies));
		}
	}

	private void validateSkills() {
		log.debug("Validating skill consistency");
		Set<String> skills = skillService.getAllSkills()
				.stream()
				.map(SkillDictionary::name)
				.collect(Collectors.toSet());

		HashSet<String> localizedSkills = new HashSet<>(localization.getSkills().keySet());
		localizedSkills.retainAll(skills);
		if (localizedSkills.size() != skills.size()) {
			throw new IllegalStateException(LOCALIZATION_ERROR_MSG_TEMPLATE.formatted(SKILLS, skills, localizedSkills));
		}
	}
}
