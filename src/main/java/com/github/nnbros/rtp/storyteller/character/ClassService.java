package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.jpa.character.ClassDictionaryEntity;
import com.github.nnbros.rtp.storyteller.jpa.character.ClassDictionaryMapper;
import com.github.nnbros.rtp.storyteller.jpa.character.SkillDictionaryMapper;
import com.github.nnbros.rtp.storyteller.repository.ClassDictionaryRepository;
import com.github.nnbros.rtp.storyteller.repository.SkillDictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ClassService {
	private final ClassDictionaryMapper classDictionaryMapper;
	private final SkillDictionaryMapper skillDictionaryMapper;
	private final ClassDictionaryRepository classDictionaryRepository;
	private final SkillDictionaryRepository skillDictionaryRepository;

	public Collection<ClassDictionary> getAllClasses() {
		return classDictionaryRepository.findAll()
				.stream()
				.map(classDictionaryMapper::toClassDictionary)
				.toList();
	}

	public Collection<SkillDictionary> getAllSkillsByClassName(String name) {
		ClassDictionaryEntity entity = classDictionaryRepository.findSkillsByName(name)
				.orElseThrow(() -> new StoryTellerRuntimeException("Unable to find class id by name [%s]", name));
		return entity.getSkills()
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

	public int getClassIdByName(String name) {
		return classDictionaryRepository.findIdByName(name)
				.orElseThrow(() -> new StoryTellerRuntimeException("Unable to find class id by name [%s]", name));
	}

	public boolean containsByName(String className) {
		return classDictionaryRepository.findIdByName(className).isPresent();
	}
}
