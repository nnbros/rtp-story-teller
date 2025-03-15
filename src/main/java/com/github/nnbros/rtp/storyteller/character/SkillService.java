package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.storyteller.jpa.character.SkillDictionaryMapper;
import com.github.nnbros.rtp.storyteller.repository.SkillDictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class SkillService {
	private final SkillDictionaryRepository skillDictionaryRepository;
	private final SkillDictionaryMapper skillDictionaryMapper;

	public Collection<SkillDictionary> getAllSkillsByClassName(@NonNull String name) {
		return skillDictionaryRepository.findSkillsByType(SkillType.valueOf(name.toUpperCase()))
				.stream()
				.map(skillDictionaryMapper::toSkillDictionary)
				.toList();
	}

	public Collection<SkillDictionary> getAllSkills() {
		return skillDictionaryRepository.findAll()
				.stream()
				.map(skillDictionaryMapper::toSkillDictionary)
				.toList();
	}
}
