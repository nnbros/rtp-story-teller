package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.Character;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import com.github.nnbros.rtp.storyteller.telegram.AbstractTelegramClient;
import com.github.nnbros.rtp.storyteller.telegram.ui.ParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;

@Slf4j
@Service
public class MainMenuTelegramClient extends AbstractTelegramClient {
	private final CharacterService characterService;
	private final ParameterService parameterService;

	public MainMenuTelegramClient(TelegramClient telegramClient,
								  TelegramElementRegistry elementRegistry, CharacterService characterService,
								  ParameterService parameterService) {
		super(telegramClient, elementRegistry);
		this.characterService = characterService;
		this.parameterService = parameterService;
	}

	public void sendMainMenu(ActionContext actionContext) {
		Long userId = actionContext.userId();
		log.debug("Sending main menu to the user [{}]...", userId);
		Character character = characterService.getByUserId(userId);
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
		Character character = characterService.getByUserId(userId);
		Map<String, String> params = parameterService.buildCharacterBaseParameters(actionContext, character);
		BotApiMethod<?> message = buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.character.name(), params);
		execute(message);
		log.debug("Character menu has been sent successfully");
	}
}
