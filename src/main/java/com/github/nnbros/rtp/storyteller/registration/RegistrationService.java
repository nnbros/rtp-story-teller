package com.github.nnbros.rtp.storyteller.registration;

import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.ActionResult;
import com.github.nnbros.rtp.storyteller.character.CharacterRequest;
import com.github.nnbros.rtp.storyteller.character.Gender;
import com.github.nnbros.rtp.storyteller.jpa.character.CharacterService;
import com.github.nnbros.rtp.storyteller.configuration.StoryTellerProperties;
import com.github.nnbros.rtp.storyteller.exception.CharacterNotFoundException;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.jpa.character.ClassService;
import com.github.nnbros.rtp.storyteller.telegram.UpdateType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
	private final CharacterService characterService;
	private final StoryTellerProperties properties;
	private final ClassService classService;
	private final Map<Long, CharacterRequest> characterCache = new HashMap<>();

	public void createCharacter(ActionContext actionContext) {
		Long userId = actionContext.userId();
		log.debug("Character creation request has been received for the user [{}]", userId);
		if (characterService.containsByUser(userId)) {
			throw new StoryTellerRuntimeException("Character for user [%s] already exists", userId);
		}
		CharacterRequest characterRequest = new CharacterRequest(userId);
		characterCache.put(userId, characterRequest);
		log.debug("Character request has been added in cache");
	}

	public CharacterRequest addGender(ActionContext actionContext) {
		Long userId = actionContext.userId();
		log.debug("Add gender request has been received for the user [{}]", userId);
		CharacterRequest characterRequest = getCharacterRequest(userId);

		Gender gender = Optional.ofNullable(actionContext.data())
				.map(String::toUpperCase)
				.map(Gender::valueOf)
				.orElseThrow(() -> new StoryTellerRuntimeException("Character gender cannot be empty"));

		characterRequest.setGender(gender);
		log.debug("Gender {} has been added successfully", gender);
		return characterRequest;
	}

	public ActionResult<CharacterRequest> addName(ActionContext actionContext) {
		Long userId = actionContext.userId();
		log.debug("Add name request has been received for the user [{}]", userId);
		CharacterRequest characterRequest = getCharacterRequest(userId);
		UpdateType updateType = actionContext.updateType();
		if (!(updateType == UpdateType.MESSAGE || updateType == UpdateType.EDITED_MESSAGE)) {
			throw new StoryTellerRuntimeException("Wrong update type: [%s]", updateType);
		}
		Update update = actionContext.update();
		String name = update.getMessage().getText();
		boolean characterNameValid = isCharacterNameValid(name);
		if (characterNameValid) {
			characterRequest.setName(name);
			log.debug("Name {} has been added successfully", name);
		} else {
			log.debug("Name {} is illegal", name);
		}
		return new ActionResult<>(characterRequest, characterNameValid);
	}

	public CharacterRequest addClass(ActionContext actionContext) {
		Long userId = actionContext.userId();
		log.debug("Add class request has been received for the user [{}]", userId);
		CharacterRequest characterRequest = getCharacterRequest(userId);

		String className = Optional.ofNullable(actionContext.data())
				.filter(classService::containsByName)
				.orElseThrow(() -> new StoryTellerRuntimeException("Character class cannot be empty and must exist"));
		characterRequest.setClassName(className);
		log.debug("Class {} has been added successfully", className);
		return characterRequest;
	}

	public void register(ActionContext actionContext) {
		Long userId = actionContext.userId();
		log.info("Registration request has been received for the user [{}]", userId);
		CharacterRequest characterRequest = getCharacterRequest(userId);
		characterService.register(characterRequest);
		log.info("Character [{}] has been registered successfully", characterRequest);
	}

	public CharacterRequest getCharacterRequest(ActionContext actionContext) {
		Long userId = actionContext.userId();
		return getCharacterRequest(userId);
	}

	public void clearCharacterCache(Set<Long> userIds) {
		log.debug("Clear character cache request has been received, user ids: {}", userIds);
		if (userIds.isEmpty()) {
			log.debug("User ids is empty, no need to clear character cache");
		}
		characterCache.entrySet()
				.removeIf(entry -> userIds.contains(entry.getKey()));
		log.debug("Character cache has been cleared successfully");
	}

	private CharacterRequest getCharacterRequest(long userId) {
		return Optional.ofNullable(characterCache.get(userId))
				.orElseThrow(() -> new CharacterNotFoundException(userId));
	}

	private boolean isCharacterNameValid(String name) {
		String characterNamePattern = properties.getCharacterNamePattern();
		return name.matches(characterNamePattern);
	}
}
