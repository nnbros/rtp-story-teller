package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.Character;
import org.mapstruct.Mapper;

@Mapper
public interface CharacterMapper {

	Character toCharacter(CharacterEntity source);

	CharacterEntity toCharacterEntity(Character source);
}
