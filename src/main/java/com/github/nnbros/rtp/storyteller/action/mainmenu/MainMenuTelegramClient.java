package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.Character;
import com.github.nnbros.rtp.storyteller.character.CharacterClass;
import com.github.nnbros.rtp.storyteller.character.CharacterParameter;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.telegram.AbstractTelegramClient;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;

@Slf4j
@Service
public class MainMenuTelegramClient extends AbstractTelegramClient {
	private final CharacterService characterService;

	public MainMenuTelegramClient(TelegramClient telegramClient,
								  TelegramElementRegistry elementRegistry,
								  CharacterService characterService) {
		super(telegramClient, elementRegistry);
		this.characterService = characterService;
	}

	public void sendMainMenu(ActionContext actionContext) {
		log.debug("Sending main menu to the user [{}]...", actionContext.userId());
		Map<String, String> params = buildCharacterBaseParameters(actionContext);
		BotApiMethod<?> message = buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.main.name(), params);
		execute(message);
		log.debug("Main menu has been sent successfully");
	}

	public void sendMonsterHuntMenu(ActionContext actionContext) {
		log.warn("Monster hunting has not been implemented yet");
	}

	public void sendCharacterMenu(ActionContext actionContext) {
		log.debug("Sending character menu to the user [{}]...", actionContext.userId());
		Map<String, String> params = buildCharacterBaseParameters(actionContext);
		BotApiMethod<?> message = buildBotApiMethod(MainMenuElement.MAIN_MENU_GROUP_NAME, MainMenuElement.character.name(), params);
		execute(message);
		log.debug("Character menu has been sent successfully");
	}

	public void sendCharacterDetails(ActionContext actionContext) {
		log.warn("Character details have not been implemented yet");
	}

	public void sendCharacterDeckBuilder(ActionContext actionContext) {
		log.warn("Deck builder has not been implemented yet");
	}

	private Map<String, String> buildCharacterBaseParameters(ActionContext actionContext) {
		Character character = characterService.getByUserId(actionContext.userId());
		return Parameters.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, actionContext.userId()),
				Parameter.of(CharacterParameter.CHAR_NAME, character.name()),
				Parameter.of(CharacterParameter.CHAR_CLASS, character.activeClassDictionary().name())
		);
	}
}
