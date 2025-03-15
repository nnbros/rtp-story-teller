package com.github.nnbros.rtp.storyteller.telegram.ui;

import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.ArmyDictionary;
import com.github.nnbros.rtp.storyteller.character.Character;
import com.github.nnbros.rtp.storyteller.character.CharacterParameter;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ParameterService {
	private final Localization localization;

	public Map<String, String> buildCharacterBaseParameters(ActionContext actionContext, Character character, Parameter... additionalParams) {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(Parameter.of(DefaultParameter.CHAT_ID, actionContext.userId()));
		parameters.add(Parameter.of(CharacterParameter.CHAR_NAME, character.name()));
		parameters.addAll(buildClassParameters(character.activeClassDictionary()));
		parameters.addAll(buildArmyParameters(character.activeArmyDictionary()));
		parameters.addAll(Arrays.asList(additionalParams));
		return Parameters.buildParameters(parameters);
	}

	private Collection<Parameter> buildClassParameters(ClassDictionary classDictionary) {
		Localization.Clazz clazz = localization.getClasses()
				.get(classDictionary.name());
		return Set.of(
				Parameter.of(CharacterParameter.CHAR_CLASS, clazz.getName()),
				Parameter.of(CharacterParameter.CHAR_CLASS_DESCRIPTION, clazz.getDescription())
		);
	}

	private Collection<Parameter> buildArmyParameters(ArmyDictionary armyDictionary) {
		Localization.Army army = localization.getArmies()
				.get(armyDictionary.name());
		return Set.of(
				Parameter.of(CharacterParameter.CHAR_ARMY, army.getName()),
				Parameter.of(CharacterParameter.CHAR_ARMY_TYPE, army.getType()),
				Parameter.of(CharacterParameter.CHAR_ARMY_HP, armyDictionary.baseHp()),
				Parameter.of(CharacterParameter.CHAR_ARMY_COUNT, armyDictionary.baseCount()),
				Parameter.of(CharacterParameter.CHAR_ARMY_ATK, armyDictionary.baseAtk()),
				Parameter.of(CharacterParameter.CHAR_ARMY_DEF, armyDictionary.baseDef())
		);
	}
}
