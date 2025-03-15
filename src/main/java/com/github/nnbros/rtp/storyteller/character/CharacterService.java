package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.registration.CharacterRequest;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterService {
	private final CharacterRepository characterRepository;
	private final CharacterClassRepository characterClassRepository;
	private final CharacterArmyRepository characterArmyRepository;
	private final ClassService classService;
	private final ArmyService armyService;
	private final CharacterMapper characterMapper;

	@Transactional
	public Character getByUserId(long userId) {
		return characterRepository.findWithActiveClassDictionaryByUserId(userId)
				.map(characterMapper::toCharacter)
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
		characterEntity.setActiveClassDictionaryId(activeClassId);
		characterRepository.saveAndFlush(characterEntity);

		classes.values()
				.forEach(classDictionary -> saveCharacterClass(characterEntity, classDictionary.id()));
		armies.values()
				.forEach(armyDictionary -> saveCharacterArmy(characterEntity, armyDictionary.id()));
		log.info("Character has been successfully registered");
	}

	@Transactional
	public boolean updateCharacterActiveClass(long userId, String className) {
		int classId = classService.getClassIdByName(className);
		CharacterEntity characterEntity = characterRepository.findByUserId(userId)
				.orElseThrow(() -> new CharacterNotFoundException(userId));
		if (characterEntity.getActiveClassDictionaryId() != classId) {
			characterEntity.setActiveClassDictionaryId(classId);
			characterRepository.saveAndFlush(characterEntity);
			return true;
		}
		return false;
	}

	@Transactional
	public boolean updateCharacterActiveArmy(long userId, String armyName) {
		int armyId = armyService.getArmyIdByName(armyName);
		CharacterEntity characterEntity = characterRepository.findByUserId(userId)
				.orElseThrow(() -> new CharacterNotFoundException(userId));
		if (characterEntity.getActiveArmyDictionaryId() != armyId) {
			characterEntity.setActiveArmyDictionaryId(armyId);
			characterRepository.saveAndFlush(characterEntity);
			return true;
		}
		return false;
	}

	private void saveCharacterClass(CharacterEntity characterEntity, int classId) {
		CharacterClassEntity characterClassEntity = new CharacterClassEntity();
		characterClassEntity.setCharacter(characterEntity);
		characterClassEntity.setClassDictionaryId(classId);
		characterClassRepository.saveAndFlush(characterClassEntity);
	}

	private void saveCharacterArmy(CharacterEntity characterEntity, int armyId) {
		CharacterArmyEntity characterArmyEntity = new CharacterArmyEntity();
		characterArmyEntity.setCharacter(characterEntity);
		characterArmyEntity.setArmyDictionaryId(armyId);
		characterArmyRepository.saveAndFlush(characterArmyEntity);
	}
}
