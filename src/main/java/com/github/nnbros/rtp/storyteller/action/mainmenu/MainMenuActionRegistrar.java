package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.common.action.ActionPipeline;
import com.github.nnbros.rtp.common.action.ActionRegistrar;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.github.nnbros.rtp.common.action.ActionPipelines.*;
import static com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuAction.*;

@Getter
@Component
public class MainMenuActionRegistrar implements ActionRegistrar {
	private final Map<String, ActionPipeline> actionPipelines;

	public MainMenuActionRegistrar(CharacterService characterService,
								   MainMenuTelegramClient telegramClient,
								   CharacterProcessor characterProcessor) {
		actionPipelines = initPipelines(characterService, telegramClient, characterProcessor);
	}

	private static Map<String, ActionPipeline> initPipelines(CharacterService characterService,
															 MainMenuTelegramClient telegramClient,
															 CharacterProcessor characterProcessor) {
		return Map.of(
				MAIN_MENU.getActionName(), create(characterService::validateCharacter, telegramClient::sendMainMenu),
				MONSTER_HUNT.getActionName(), create(characterService::validateCharacter, telegramClient::sendMonsterHuntMenu),
				CHARACTER.getActionName(), create(characterService::validateCharacter, telegramClient::sendCharacterMenu),
				ARMY.getActionName(), create(characterProcessor::updateActiveArmy, telegramClient::sendArmyMenu, emptyConsumer())
		);
	}
}
