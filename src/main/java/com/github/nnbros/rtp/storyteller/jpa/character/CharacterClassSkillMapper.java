package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.CharacterClassSkill;
import org.mapstruct.Mapper;

@Mapper
public interface CharacterClassSkillMapper {

	CharacterClassSkill toCharacterClassSkill(CharacterClassSkillEntity source);

	CharacterClassSkillEntity toCharacterClassSkillEntity(CharacterClassSkill source);
}
