package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import org.mapstruct.Mapper;

@Mapper
public interface ClassDictionaryMapper {

	ClassDictionary toClassDictionary(ClassDictionaryEntity source);

	ClassDictionaryEntity toClassDictionaryEntity(ClassDictionary source);
}
