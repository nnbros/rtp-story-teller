package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
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
	private final ParameterService parameterService;

	public MainMenuTelegramClient(TelegramClient telegramClient,
								  TelegramElementRegistry elementRegistry,
								  ParameterService parameterService) {
		super(telegramClient, elementRegistry);
		this.parameterService = parameterService;
	}

	public void sendMainMenu(ActionContext actionContext) {
		log.debug("Sending main menu to the user [{}]...", actionContext.userId());
		Map<String, String> params = parameterService.buildCharacterBaseParameters(actionContext);
		BotApiMethod<?> message = buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.main.name(), params);
		execute(message);
		log.debug("Main menu has been sent successfully");
	}

	public void sendMonsterHuntMenu(ActionContext actionContext) {
		log.warn("Monster hunting has not been implemented yet");
	}

	public void sendCharacterMenu(ActionContext actionContext) {
		log.debug("Sending character menu to the user [{}]...", actionContext.userId());
		Map<String, String> params = parameterService.buildCharacterBaseParameters(actionContext);
		BotApiMethod<?> message = buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.character.name(), params);
		execute(message);
		log.debug("Character menu has been sent successfully");
	}
}
