package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.parameter.InlineKeyboardButtonParameters;
import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.Character;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.ClassService;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import com.github.nnbros.rtp.storyteller.telegram.AbstractTelegramClient;
import com.github.nnbros.rtp.storyteller.telegram.ui.ParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;
import java.util.Objects;

import static com.github.nnbros.rtp.storyteller.action.mainmenu.CharacterMenuAction.CLASS;
import static com.github.nnbros.rtp.storyteller.action.registration.RegistrationElement.confirmationEmoji;

@Slf4j
@Service
public class CharacterMenuTelegramClient extends AbstractTelegramClient {
	private final ClassService classService;
	private final CharacterService characterService;
	private final ParameterService parameterService;
	private final Localization localization;

	public CharacterMenuTelegramClient(TelegramClient telegramClient,
									   TelegramElementRegistry elementRegistry,
									   ClassService classService, CharacterService characterService,
									   ParameterService parameterService, Localization localization) {
		super(telegramClient, elementRegistry);
		this.classService = classService;
		this.characterService = characterService;
		this.parameterService = parameterService;
		this.localization = localization;
	}

	public void sendCharacterDetails(ActionContext actionContext) {
		log.warn("Character details have not been implemented yet");
	}

	public void sendCharacterDeckBuilder(ActionContext actionContext) {
		log.warn("Deck builder has not been implemented yet");
	}

	public void sendCharacterClassMenu(ActionContext actionContext) {
		log.debug("Sending character class menu to the user [{}]...", actionContext.userId());
		String activeClass;
		String data = actionContext.data();
		Character character = characterService.getByUserId(actionContext.userId());
		if (Objects.nonNull(data)) {
			activeClass = data;
		} else {
			activeClass = character.activeClassDictionary().name();
		}

		String confirmationEmojiText = buildText(confirmationEmoji);
		Map<String, Localization.Clazz> localizedClasses = localization.getClasses();
		InlineKeyboardButtonParameters dynamicParams = new InlineKeyboardButtonParameters();
		classService.getAllClasses()
				.stream()
				.map(ClassDictionary::name)
				.forEach(className -> addClassDynamicParameter(dynamicParams, className, activeClass, localizedClasses, confirmationEmojiText));

		Parameter activeClassDescription = Parameter.of(CharacterMenuParameter.CLASS_DESCRIPTION, localizedClasses.get(activeClass).getDescription());
		Map<String, String> params = parameterService.buildCharacterBaseParameters(actionContext, character, activeClassDescription);
		BotApiMethod<?> message = buildBotApiMethod(
				MainMenuElement.MAIN_MENU_GROUP_NAME,
				MainMenuElement.charClass.name(),
				params,
				Map.of(CharacterMenuParameter.CLASSES.getKey(), dynamicParams));
		execute(message);
		log.debug("Character character class menu has been sent successfully");
	}

	private void addClassDynamicParameter(InlineKeyboardButtonParameters parameters,
										  String className,
										  String activeClass,
										  Map<String, Localization.Clazz> localizedClasses,
										  String confirmationEmoji) {
		String localizedClassName = localizedClasses.get(className).getName();
		String finalText = className.equals(activeClass) ? CONFIRMED_OPTION_TEMPLATE.formatted(confirmationEmoji, localizedClassName) : localizedClassName;
		parameters.add(finalText, CALLBACK_DATA_TEMPLATE.formatted(CLASS.getActionName(), className));
	}
}
