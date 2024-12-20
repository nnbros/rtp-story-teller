package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.storyteller.action.ActionPipeline;
import com.github.nnbros.rtp.storyteller.action.ActionRegistrar;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.github.nnbros.rtp.storyteller.action.ActionPipelines.create;
import static com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuAction.*;

@Component
public class MainMenuActionRegistrar implements ActionRegistrar {
	private final Map<String, ActionPipeline> actionPipelines;

	public MainMenuActionRegistrar(CharacterService characterService,
								   MainMenuTelegramClient telegramClient) {
		actionPipelines = initPipelines(characterService, telegramClient);
	}

	private static Map<String, ActionPipeline> initPipelines(CharacterService characterService, MainMenuTelegramClient telegramClient) {
		return Map.of(
				MAIN_MENU.getActionName(), create(characterService::validateCharacter, telegramClient::sendMainMenu),
				MONSTER_HUNT.getActionName(), create(characterService::validateCharacter, telegramClient::sendMonsterHuntMenu),
				CHARACTER.getActionName(), create(characterService::validateCharacter, telegramClient::sendCharacterMenu),
				CHARACTER_DETAILS.getActionName(), create(characterService::validateCharacter, telegramClient::sendCharacterDetails),
				CHARACTER_DECK_BUILDER.getActionName(), create(characterService::validateCharacter, telegramClient::sendCharacterDeckBuilder)
		);
	}

	@Override
	public void register(Map<String, ActionPipeline> actionPipelines) {
		actionPipelines.putAll(this.actionPipelines);
	}
}
