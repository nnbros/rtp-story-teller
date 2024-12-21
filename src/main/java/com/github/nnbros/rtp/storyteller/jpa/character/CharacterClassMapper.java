package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.CharacterClass;
import org.mapstruct.Mapper;

@Mapper
public interface CharacterClassMapper {

	CharacterClass toCharacterClass(CharacterClassEntity source);

	CharacterClassEntity toCharacterClassEntity(CharacterClass source);
}
