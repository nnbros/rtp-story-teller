package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.registration.CharacterRequest;
import com.github.nnbros.rtp.storyteller.exception.CharacterArmyNotFoundException;
import com.github.nnbros.rtp.storyteller.exception.CharacterClassNotFoundException;
import com.github.nnbros.rtp.storyteller.exception.CharacterNotFoundException;
import com.github.nnbros.rtp.storyteller.jpa.character.CharacterArmyEntity;
import com.github.nnbros.rtp.storyteller.jpa.character.CharacterClassEntity;
import com.github.nnbros.rtp.storyteller.jpa.character.CharacterEntity;
import com.github.nnbros.rtp.storyteller.jpa.character.CharacterMapper;
import com.github.nnbros.rtp.storyteller.repository.CharacterArmyRepository;
import com.github.nnbros.rtp.storyteller.repository.CharacterClassRepository;
import com.github.nnbros.rtp.storyteller.repository.CharacterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterService {
	//TODO remove when army is selected during registration process
	public static final int DEFAULT_ACTIVE_ARMY_ID = 1;

	private final CharacterRepository characterRepository;
	private final CharacterClassRepository characterClassRepository;
	private final CharacterArmyRepository characterArmyRepository;
	private final ClassService classService;
	private final ArmyService armyService;
	private final SkillService skillService;
	private final CharacterMapper characterMapper;

	@Transactional
	public DetailedCharacterView getDetailedCharacterViewByUserId(long userId) {
		return characterRepository.findWithActiveClassAndArmyByUserId(userId)
				.map(characterMapper::toDetailedCharacterView)
				.orElseThrow(() -> new CharacterNotFoundException(userId));
	}

	@Transactional
	public DetailedCharacterWithSkillsView getDetailedCharacterWithSkillsViewByUserId(long userId) {
		return characterRepository.findWithActiveClassArmyAndSkillsByUserId(userId)
				.map(characterMapper::toDetailedCharacterWithSkillsView)
				.orElseThrow(() -> new CharacterNotFoundException(userId));
	}

	public boolean containsByUser(long userId) {
		return characterRepository.findByUserId(userId).isPresent();
	}

	//TODO caching, can be used at the beginning of the processing
	public void validateCharacter(ActionContext actionContext) {
		long userId = actionContext.userId();
		if (!containsByUser(userId)) {
			throw new CharacterNotFoundException(userId);
		}
	}

	@Transactional
	public void register(CharacterRequest characterRequest) {
		log.info("Registering a new character: [{}]", characterRequest);
		String className = characterRequest.getClassName();

		//TODO Refactor when not all classes and armies are available for selecting
		Map<String, ClassDictionary> classes = classService.getAllClasses()
				.stream()
				.collect(Collectors.toMap(ClassDictionary::name, Function.identity()));
		int activeClassId = classes.get(className).id();

		Map<String, ArmyDictionary> armies = armyService.getAllArmies()
				.stream()
				.collect(Collectors.toMap(ArmyDictionary::name, Function.identity()));

		CharacterEntity characterEntity = new CharacterEntity();
		characterEntity.setName(characterRequest.getName());
		characterEntity.setUserId(characterRequest.getUserId());
		characterEntity.setGender(characterRequest.getGender());
		characterRepository.saveAndFlush(characterEntity);

		classes.values()
				.forEach(classDictionary -> saveCharacterClass(characterEntity, classDictionary, activeClassId));
		armies.values()
				.forEach(armyDictionary -> saveCharacterArmy(characterEntity, armyDictionary, DEFAULT_ACTIVE_ARMY_ID));
		log.info("Character has been successfully registered");
	}

	@Transactional
	public boolean updateCharacterActiveClass(long userId, String className) {
		CharacterClassEntity characterClassEntity = characterClassRepository.findByClassIdAndCharacterId(userId, className)
				.orElseThrow(() -> new CharacterClassNotFoundException(userId, className));
		if (!characterClassEntity.getIsActive()) {
			characterClassEntity.setIsActive(true);
			characterClassRepository.saveAndFlush(characterClassEntity);
			return true;
		}
		return false;
	}

	@Transactional
	public boolean updateCharacterActiveArmy(long userId, String armyName) {
		CharacterClassEntity characterClassEntity = characterClassRepository.findByClassIdAndCharacterId(userId, armyName)
				.orElseThrow(() -> new CharacterArmyNotFoundException(userId, armyName));
		if (!characterClassEntity.getIsActive()) {
			characterClassEntity.setIsActive(true);
			characterClassRepository.saveAndFlush(characterClassEntity);
			return true;
		}
		return false;
	}

	private void saveCharacterClass(CharacterEntity characterEntity, ClassDictionary classDictionary, int activeClassId) {
		CharacterClassEntity characterClassEntity = new CharacterClassEntity();
		characterClassEntity.setCharacter(characterEntity);
		characterClassEntity.setClassDictionaryId(classDictionary.id());
		characterClassEntity.setIsActive(classDictionary.id() == activeClassId);
		CharacterClassEntity savedCharacterClass = characterClassRepository.saveAndFlush(characterClassEntity);
		skillService.saveDefaultCharacterClassSkills(classDictionary, savedCharacterClass.getId());
	}

	private void saveCharacterArmy(CharacterEntity characterEntity, ArmyDictionary armyDictionary, int activeArmyId) {
		CharacterArmyEntity characterArmyEntity = new CharacterArmyEntity();
		characterArmyEntity.setCharacter(characterEntity);
		characterArmyEntity.setArmyDictionaryId(armyDictionary.id());
		characterArmyEntity.setIsActive(armyDictionary.id() == activeArmyId);
		CharacterArmyEntity savedCharacterArmy = characterArmyRepository.saveAndFlush(characterArmyEntity);
		skillService.saveDefaultCharacterArmySkills(armyDictionary, savedCharacterArmy.getId());
	}
}
