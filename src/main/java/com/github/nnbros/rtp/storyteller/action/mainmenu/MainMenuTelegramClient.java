package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.parameter.InlineKeyboardButtonParameters;
import com.github.nnbros.rtp.common.action.ActionContext;
import com.github.nnbros.rtp.common.action.ActionResult;
import com.github.nnbros.rtp.common.api.dto.character.DetailedCharacterView;
import com.github.nnbros.rtp.common.telegram.AbstractTelegramClient;
import com.github.nnbros.rtp.storyteller.character.*;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import com.github.nnbros.rtp.storyteller.telegram.ui.ParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;
import java.util.Objects;

import static com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuAction.ARMY;
import static com.github.nnbros.rtp.storyteller.action.registration.RegistrationElement.confirmationEmoji;

@Slf4j
@Service
public class MainMenuTelegramClient extends AbstractTelegramClient {
	private final CharacterService characterService;
	private final ParameterService parameterService;
	private final ArmyService armyService;
	private final Localization localization;

	public MainMenuTelegramClient(TelegramClient telegramClient,
								  TelegramElementRegistry elementRegistry, CharacterService characterService,
								  ParameterService parameterService, ArmyService armyService, Localization localization) {
		super(telegramClient, elementRegistry);
		this.characterService = characterService;
		this.parameterService = parameterService;
		this.armyService = armyService;
		this.localization = localization;
	}

	public void sendMainMenu(ActionContext actionContext) {
		Long userId = actionContext.userId();
		log.debug("Sending main menu to the user [{}]...", userId);
		DetailedCharacterView character = characterService.getDetailedCharacterViewByUserId(userId);
		Map<String, String> params = parameterService.buildCharacterBaseParameters(actionContext, character);
		BotApiMethod<?> message = buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.main.name(), params);
		execute(message);
		log.debug("Main menu has been sent successfully");
	}

	public void sendMonsterHuntMenu(ActionContext actionContext) {
		log.warn("Monster hunting has not been implemented yet");
	}

	public void sendCharacterMenu(ActionContext actionContext) {
		Long userId = actionContext.userId();
		log.debug("Sending character menu to the user [{}]...", actionContext.userId());
		DetailedCharacterView character = characterService.getDetailedCharacterViewByUserId(userId);
		Map<String, String> params = parameterService.buildCharacterBaseParameters(actionContext, character);
		BotApiMethod<?> message = buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.character.name(), params);
		execute(message);
		log.debug("Character menu has been sent successfully");
	}

	public void sendArmyMenu(ActionResult<?> actionResult) {
		ActionContext actionContext = actionResult.getActionContext();
		log.debug("Sending character army menu to the user [{}]...", actionContext.userId());
		String activeArmy;
		String data = actionContext.actionData();
		DetailedCharacterView character = characterService.getDetailedCharacterViewByUserId(actionContext.userId());
		if (Objects.nonNull(data)) {
			activeArmy = data;
		} else {
			activeArmy = character.getActiveArmy().name();
		}

		String confirmationEmojiText = buildText(confirmationEmoji);
		Map<String, Localization.Army> localizedArmies = localization.getArmies();
		InlineKeyboardButtonParameters dynamicParams = new InlineKeyboardButtonParameters();
		armyService.getAllArmies()
				.stream()
				.map(ArmyDictionary::name)
				.forEach(className -> addArmyDynamicParameter(dynamicParams, className, activeArmy, localizedArmies, confirmationEmojiText));

		Map<String, String> params = parameterService.buildCharacterBaseParameters(actionContext, character);
		BotApiMethod<?> message = buildBotApiMethod(
				MainMenuElement.MAIN_MENU_GROUP_NAME,
				MainMenuElement.army.name(),
				params,
				Map.of(MainMenuParameter.ARMIES.getKey(), dynamicParams));
		execute(message);
		log.debug("Character character army menu has been sent successfully");
	}

	private void addArmyDynamicParameter(InlineKeyboardButtonParameters parameters,
										  String armyName,
										  String activeArmy,
										  Map<String, Localization.Army> localizedArmies,
										  String confirmationEmoji) {
		String localizedArmyName = localizedArmies.get(armyName).getName();
		String finalText = armyName.equals(activeArmy) ? CONFIRMED_OPTION_TEMPLATE.formatted(confirmationEmoji, localizedArmyName) : localizedArmyName;
		parameters.add(finalText, CALLBACK_DATA_TEMPLATE.formatted(ARMY.getActionName(), armyName));
	}
}
