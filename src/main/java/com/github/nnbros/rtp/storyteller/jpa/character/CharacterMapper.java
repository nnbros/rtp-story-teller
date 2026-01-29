package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.common.api.dto.character.DetailedCharacterView;
import com.github.nnbros.rtp.common.api.dto.character.DetailedCharacterWithSkillsView;
import org.mapstruct.Mapper;

@Mapper
public interface CharacterMapper {

	Character toCharacter(CharacterEntity source);

	DetailedCharacterView toDetailedCharacterView(CharacterEntity source);

	DetailedCharacterWithSkillsView toDetailedCharacterWithSkillsView(CharacterEntity source);
}
