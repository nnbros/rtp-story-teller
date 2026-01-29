package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.common.telegram.ui.Element;

public enum MainMenuElement implements Element {
	main,
	character,
	charClass,
	army;

	public static final String MAIN_MENU_GROUP_NAME = "mainMenu";

	@Override
	public String getGroupName() {
		return MAIN_MENU_GROUP_NAME;
	}
}
