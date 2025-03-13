package com.github.nnbros.rtp.storyteller.telegram.ui;

import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.Character;
import com.github.nnbros.rtp.storyteller.character.CharacterParameter;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParameterService {
	private final Localization localization;

	public Map<String, String> buildCharacterBaseParameters(ActionContext actionContext, Character character, Parameter... additionalParams) {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(Parameter.of(DefaultParameter.CHAT_ID, actionContext.userId()));
		parameters.add(Parameter.of(CharacterParameter.CHAR_NAME, character.name()));
		String localizedClassName = localization.getClasses()
				.get(character.activeClassDictionary().name())
				.getName();
		parameters.add(Parameter.of(CharacterParameter.CHAR_CLASS, localizedClassName));
		parameters.addAll(Arrays.asList(additionalParams));
		return Parameters.buildParameters(parameters);
	}
}
