package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.CharacterArmy;
import org.mapstruct.Mapper;

@Mapper
public interface CharacterArmyMapper {

	CharacterArmy toCharacterArmy(CharacterArmyEntity source);

	CharacterArmyEntity toCharacterArmyEntity(CharacterArmy source);
}
