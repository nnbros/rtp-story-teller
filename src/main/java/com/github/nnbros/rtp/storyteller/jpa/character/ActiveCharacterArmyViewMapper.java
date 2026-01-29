package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.common.api.dto.character.ActiveCharacterArmy;
import org.mapstruct.Mapper;

@Mapper
public interface ActiveCharacterArmyViewMapper {

	ActiveCharacterArmy toActiveCharacterArmyView(ActiveCharacterArmyViewEntity source);

	ActiveCharacterArmyViewEntity toActiveCharacterArmyViewEntity(ActiveCharacterArmy source);
}
