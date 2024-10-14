package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.SkillDictionary;
import org.mapstruct.Mapper;

@Mapper
public interface SkillDictionaryMapper {

	SkillDictionary toSkillDictionary(SkillDictionaryEntity source);

	SkillDictionaryEntity toSkillDictionaryEntity(SkillDictionary source);
}
