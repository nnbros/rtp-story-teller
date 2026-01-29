package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.common.action.Action;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterMenuAction implements Action {
    CHARACTER_DETAILS("storyteller_main_menu_character_details"),
    CHARACTER_DECK_BUILDER("storyteller_main_menu_character_deck_builder"),
    CLASS("storyteller_main_menu_character_class");

    private final String actionName;
}
