package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.storyteller.jpa.character.CharacterArmySkillEntity;
import com.github.nnbros.rtp.storyteller.jpa.character.CharacterClassSkillEntity;
import com.github.nnbros.rtp.storyteller.jpa.character.SkillDictionaryMapper;
import com.github.nnbros.rtp.storyteller.repository.CharacterArmySkillRepository;
import com.github.nnbros.rtp.storyteller.repository.CharacterClassSkillRepository;
import com.github.nnbros.rtp.storyteller.repository.SkillDictionaryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.github.nnbros.rtp.storyteller.character.SkillType.BASIC_ARMY;
import static com.github.nnbros.rtp.storyteller.character.SkillType.BASIC_CHARACTER;

@Service
@RequiredArgsConstructor
//TODO caching
public class SkillService {
	private final SkillDictionaryRepository skillDictionaryRepository;
	private final SkillDictionaryMapper skillDictionaryMapper;
	private final CharacterClassSkillRepository characterClassSkillRepository;
	private final CharacterArmySkillRepository characterArmySkillRepository;

	public Collection<SkillDictionary> getAllSkillsByClassName(@NonNull String name) {
		return skillDictionaryRepository.findSkillsByType(SkillType.valueOf(name.toUpperCase()))
				.stream()
				.map(skillDictionaryMapper::toSkillDictionary)
				.toList();
	}

	@Transactional
	public void saveDefaultCharacterClassSkills(@NonNull ClassDictionary classDictionary, int characterClassId) {
		Set<SkillType> skillTypes = getAllowedClassSkillTypes(classDictionary);
		List<CharacterClassSkillEntity> characterClassSkills = skillDictionaryRepository.findSkillsByTypes(skillTypes)
				.stream()
				.map(skillDictionary -> createCharacterClassSkillEntyty(characterClassId, skillDictionary.getId()))
				.toList();
		characterClassSkillRepository.saveAllAndFlush(characterClassSkills);
	}

	@Transactional
	public void saveDefaultCharacterArmySkills(@NonNull ArmyDictionary armyDictionary, int characterArmyId) {
		Set<SkillType> skillTypes = getAllowedArmySkillTypes(armyDictionary);
		List<CharacterArmySkillEntity> characterArmySkill = skillDictionaryRepository.findSkillsByTypes(skillTypes)
				.stream()
				.map(skillDictionary -> createCharacterArmySkillEntyty(characterArmyId, skillDictionary.getId()))
				.toList();
		characterArmySkillRepository.saveAllAndFlush(characterArmySkill);
	}

	public Collection<SkillDictionary> getAllSkills() {
		return skillDictionaryRepository.findAll()
				.stream()
				.map(skillDictionaryMapper::toSkillDictionary)
				.toList();
	}

	private Set<SkillType> getAllowedClassSkillTypes(ClassDictionary classDictionary) {
		SkillType classSkillType = SkillType.valueOf(classDictionary.name().toUpperCase());
		return EnumSet.of(BASIC_CHARACTER, classSkillType);
	}

	private Set<SkillType> getAllowedArmySkillTypes(ArmyDictionary armyDictionary) {
		SkillType armySkillType = SkillType.valueOf(armyDictionary.name().toUpperCase());
		SkillType archetypeSkillType = SkillType.valueOf(armyDictionary.type().name());
		return EnumSet.of(BASIC_ARMY, armySkillType, archetypeSkillType);
	}

	private CharacterClassSkillEntity createCharacterClassSkillEntyty(int characterClassId, int skillId) {
		CharacterClassSkillEntity characterClassSkillEntity = new CharacterClassSkillEntity();
		characterClassSkillEntity.setCharacterClassId(characterClassId);
		characterClassSkillEntity.setSkillId(skillId);
		return characterClassSkillEntity;
	}

	private CharacterArmySkillEntity createCharacterArmySkillEntyty(int characterArmyId, int skillId) {
		CharacterArmySkillEntity characterArmySkillEntity = new CharacterArmySkillEntity();
		characterArmySkillEntity.setCharacterArmyId(characterArmyId);
		characterArmySkillEntity.setSkillId(skillId);
		return characterArmySkillEntity;
	}
}
