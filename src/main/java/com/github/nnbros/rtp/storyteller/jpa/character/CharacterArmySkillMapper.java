package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.CharacterArmySkill;
import org.mapstruct.Mapper;

@Mapper
public interface CharacterArmySkillMapper {

	CharacterArmySkill toCharacterArmySkill(CharacterArmySkillEntity source);

	CharacterArmySkillEntity toCharacterArmySkillEntity(CharacterArmySkill source);
}
