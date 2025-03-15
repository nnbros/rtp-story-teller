package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.ArmyDictionary;
import org.mapstruct.Mapper;

@Mapper
public interface ArmyDictionaryMapper {

	ArmyDictionary toArmyDictionary(ArmyDictionaryEntity source);

	ArmyDictionaryEntity toArmyDictionaryEntity(ArmyDictionary source);
}
