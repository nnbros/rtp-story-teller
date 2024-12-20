package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.storyteller.action.Action;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainMenuAction implements Action {
	MAIN_MENU("storyteller_main_menu"),
	MONSTER_HUNT("storyteller_main_menu_monster_hunt"),
	CHARACTER("storyteller_main_menu_character"),
	CHARACTER_DETAILS("storyteller_main_menu_character_details"),
	CHARACTER_DECK_BUILDER("storyteller_main_menu_character_deck_builder");

	private final String actionName;
}
