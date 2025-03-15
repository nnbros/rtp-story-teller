package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.jpa.character.ArmyDictionaryMapper;
import com.github.nnbros.rtp.storyteller.repository.ArmyDictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ArmyService {
	private final ArmyDictionaryRepository armyDictionaryRepository;
	private final ArmyDictionaryMapper armyDictionaryMapper;

	public Collection<ArmyDictionary> getAllArmies() {
		return armyDictionaryRepository.findAll()
				.stream()
				.map(armyDictionaryMapper::toArmyDictionary)
				.toList();
	}

	public int getArmyIdByName(String name) {
		return armyDictionaryRepository.findIdByName(name)
				.orElseThrow(() -> new StoryTellerRuntimeException("Unable to find army id by name [%s]", name));
	}
}
