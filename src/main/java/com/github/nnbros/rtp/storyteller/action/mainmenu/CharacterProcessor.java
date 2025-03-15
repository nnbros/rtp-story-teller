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
public class CharacterProcessor {
	private final CharacterService characterService;

	public ActionResult<ActionContext> updateActiveClass(ActionContext actionContext) {
		characterService.validateCharacter(actionContext);
		String selectedClass = actionContext.data();
		boolean sendClassMenu = true;
		if (Objects.nonNull(selectedClass)) {
			log.debug("Updating character active class: {}", selectedClass);
			Long userId = actionContext.userId();
			sendClassMenu = characterService.updateCharacterActiveClass(userId, selectedClass);
			log.debug("Character active class has been updated");
		}
		return new ActionResult<>(actionContext, sendClassMenu);
    }

    public ActionResult<ActionContext> updateActiveArmy(ActionContext actionContext) {
        String selectedArmy = actionContext.data();
        boolean sendArmyMenu = true;
        if (Objects.nonNull(selectedArmy)) {
            log.debug("Updating character active army: {}", selectedArmy);
            Long userId = actionContext.userId();
            sendArmyMenu = characterService.updateCharacterActiveArmy(userId, selectedArmy);
            log.debug("Character active army has been updated");
        }
        return new ActionResult<>(actionContext, sendArmyMenu);
	}
}
