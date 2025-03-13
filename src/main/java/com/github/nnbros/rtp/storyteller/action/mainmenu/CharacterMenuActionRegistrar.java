package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.storyteller.action.ActionPipeline;
import com.github.nnbros.rtp.storyteller.action.ActionRegistrar;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.github.nnbros.rtp.storyteller.action.ActionPipelines.create;
import static com.github.nnbros.rtp.storyteller.action.mainmenu.CharacterMenuAction.*;
import static com.github.nnbros.rtp.storyteller.util.StorytellerUtils.emptyConsumer;

@Getter
@Component
public class CharacterMenuActionRegistrar implements ActionRegistrar {
	private final Map<String, ActionPipeline> actionPipelines;

	public CharacterMenuActionRegistrar(CharacterService characterService,
										CharacterMenuService characterMenuService,
										CharacterMenuTelegramClient telegramClient) {
		actionPipelines = initPipelines(characterService, characterMenuService, telegramClient);
	}

	private static Map<String, ActionPipeline> initPipelines(CharacterService characterService,
															 CharacterMenuService characterMenuService,
															 CharacterMenuTelegramClient telegramClient) {
		return Map.of(
				CHARACTER_DETAILS.getActionName(), create(characterService::validateCharacter, telegramClient::sendCharacterDetails),
				CHARACTER_DECK_BUILDER.getActionName(), create(characterService::validateCharacter, telegramClient::sendCharacterDeckBuilder),
				CLASS.getActionName(), create(characterMenuService::updateActiveClass, telegramClient::sendCharacterClassMenu, emptyConsumer())
		);
	}

	@Override
	public void register(Map<String, ActionPipeline> actionPipelines) {
		actionPipelines.putAll(this.actionPipelines);
	}
}
