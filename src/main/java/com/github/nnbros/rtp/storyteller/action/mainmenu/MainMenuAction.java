package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.common.action.Action;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainMenuAction implements Action {
	MAIN_MENU("storyteller_main_menu"),
	MONSTER_HUNT("storyteller_main_menu_monster_hunt"),
	CHARACTER("storyteller_main_menu_character"),
	ARMY("storyteller_main_menu_army");

	private final String actionName;
}
