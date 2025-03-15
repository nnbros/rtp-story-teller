package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.jpa.character.ClassDictionaryMapper;
import com.github.nnbros.rtp.storyteller.repository.ClassDictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ClassService {
	private final ClassDictionaryMapper classDictionaryMapper;
	private final ClassDictionaryRepository classDictionaryRepository;

	public Collection<ClassDictionary> getAllClasses() {
		return classDictionaryRepository.findAll()
				.stream()
				.map(classDictionaryMapper::toClassDictionary)
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
