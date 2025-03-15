package com.github.nnbros.rtp.storyteller.character;

import com.github.guronas.telegram.bot.elements.parameter.ParameterKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterParameter implements ParameterKey {
	CHAR_GENDER("charGender"),
	CHAR_NAME("charName"),
	CHAR_CLASS("charClass"),
	ACCOUNT_LEVEL("accountLevel"),
	CHAR_CLASS_LEVEL("charClassLevel"),
	CHAR_ARMY("charArmy"),
	CHAR_ARMY_TYPE("charArmyType"),
	CHAR_ARMY_HP("charArmyHp"),
	CHAR_ARMY_COUNT("charArmyCount"),
	CHAR_ARMY_ATK("charArmyAtk"),
	CHAR_ARMY_DEF("charArmyDef"),
	CHAR_CLASS_DESCRIPTION("charClassDescription");

	private final String key;
}
