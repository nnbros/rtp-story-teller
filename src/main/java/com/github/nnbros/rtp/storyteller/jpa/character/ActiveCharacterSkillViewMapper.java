package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.common.api.dto.character.ActiveCharacterSkill;
import org.mapstruct.Mapper;

@Mapper
public interface ActiveCharacterSkillViewMapper {

	ActiveCharacterSkill toActiveCharacterSkillView(ActiveCharacterSkillViewEntity source);

	ActiveCharacterSkillViewEntity toActiveCharacterSkillViewEntity(ActiveCharacterSkill source);
}
