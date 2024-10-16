package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.CharacterRequest;
import com.github.nnbros.rtp.storyteller.repository.CharacterClassRepository;
import com.github.nnbros.rtp.storyteller.repository.CharacterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterService {
	private final CharacterRepository characterRepository;
	private final CharacterClassRepository characterClassRepository;
	private final ClassService classService;

	public boolean containsByUser(long userId) {
		return characterRepository.findByUserId(userId).isPresent();
	}

	@Transactional
	public void register(CharacterRequest characterRequest) {
		log.info("Registering a new character: [{}]", characterRequest);
		String className = characterRequest.getClassName();
		int classId = classService.getClassIdByName(className);

		CharacterEntity characterEntity = new CharacterEntity();
		characterEntity.setName(characterRequest.getName());
		characterEntity.setUserId(characterRequest.getUserId());
		characterEntity.setGender(characterRequest.getGender());
		characterEntity.setActiveClassDictionaryId(classId);
		characterRepository.saveAndFlush(characterEntity);

		CharacterClassEntity characterClassEntity = new CharacterClassEntity();
		characterClassEntity.setCharacter(characterEntity);
		characterClassEntity.setClassDictionaryId(classId);
		characterClassRepository.saveAndFlush(characterClassEntity);
		log.info("Character has been successfully registered");
	}
}
