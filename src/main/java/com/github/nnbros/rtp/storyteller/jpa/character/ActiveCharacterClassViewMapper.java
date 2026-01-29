package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.common.api.dto.character.ActiveCharacterClass;
import org.mapstruct.Mapper;

@Mapper
public interface ActiveCharacterClassViewMapper {

	ActiveCharacterClass toActiveCharacterClassView(ActiveCharacterClassViewEntity source);

	ActiveCharacterClassViewEntity toActiveCharacterClassViewEntity(ActiveCharacterClass source);
}
