package com.github.nnbros.rtp.storyteller.action.mainmenu;

import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.ActionResult;
import com.github.nnbros.rtp.storyteller.character.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterMenuService {
	private final CharacterService characterService;

	public ActionResult<ActionContext> updateActiveClass(ActionContext actionContext) {
		characterService.validateCharacter(actionContext);
		String selectedClass = actionContext.data();
		boolean showClassMenu = true;
		if (Objects.nonNull(selectedClass)) {
			log.debug("Updating character active class: {}", selectedClass);
			Long userId = actionContext.userId();
			showClassMenu = characterService.updateCharacterActiveClass(userId, selectedClass);
			log.debug("Character active class has been updated");
		}
		return new ActionResult<>(actionContext, showClassMenu);
	}
}
