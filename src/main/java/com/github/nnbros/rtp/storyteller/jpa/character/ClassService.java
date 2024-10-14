package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.SkillDictionary;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
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

	public ClassDictionary getClassByName(String name) {
		ClassDictionaryEntity classDictionaryEntity = classDictionaryRepository.findByName(name)
				.orElseThrow(() -> new StoryTellerRuntimeException("Unable to find class in class dictionary with name [%s]", name));
		return classDictionaryMapper.toClassDictionary(classDictionaryEntity);
	}

	public Collection<SkillDictionary> getAllSkillsByClassId(int classId) {
		return skillDictionaryRepository.findAllByClassId(classId)
				.stream()
				.map(skillDictionaryMapper::toSkillDictionary)
				.toList();
	}

	public int getClassIdByName(String name) {
		return classDictionaryRepository.findIdByName(name)
				.orElseThrow(() -> new StoryTellerRuntimeException("Unable to find class id by name [%s]", name));
	}
}
