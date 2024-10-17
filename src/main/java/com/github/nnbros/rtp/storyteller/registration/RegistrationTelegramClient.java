package com.github.nnbros.rtp.storyteller.registration;

import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.CharacterRequest;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.SkillDictionary;
import com.github.nnbros.rtp.storyteller.jpa.character.ClassService;
import com.github.nnbros.rtp.storyteller.telegram.AbstractTelegramClient;
import com.github.nnbros.rtp.storyteller.telegram.ui.RegistrationTelegramElementFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Collection;

@Slf4j
@Service
public class RegistrationTelegramClient extends AbstractTelegramClient {
	private final RegistrationTelegramElementFactory elementFactory;
	private final ClassService classService;

	public RegistrationTelegramClient(TelegramClient telegramClient, RegistrationTelegramElementFactory elementFactory, ClassService classService) {
		super(telegramClient);
		this.elementFactory = elementFactory;
		this.classService = classService;
	}

	public void sendGenderOptions(ActionContext actionContext) {
		log.debug("Sending character gender request to the user [{}]...", actionContext.userId());
		SendMessage genderMessage = elementFactory.characterGenderMessage(actionContext.userId());
		execute(genderMessage);
		log.debug("Character gender request has been sent successfully");
	}

	public void requestName(CharacterRequest characterRequest) {
		log.debug("Sending character name request to the user [{}]...", characterRequest.getUserId());
		SendMessage nameMessage = elementFactory.characterNameMessage(characterRequest);
		execute(nameMessage);
		log.debug("Character name request has been sent successfully");
	}

	public void sendClassOptions(CharacterRequest characterRequest) {
		log.debug("Sending character class options to the user [{}]...", characterRequest.getUserId());
		Collection<ClassDictionary> classes = classService.getAllClasses();
		SendMessage classOptionsMessage = elementFactory.classSelectionMessage(characterRequest, classes);
		execute(classOptionsMessage);
		log.debug("Class options have been sent successfully");
	}

	public void sendClassDescription(CharacterRequest characterRequest) {
		log.debug("Sending character class description to the user [{}]...", characterRequest.getUserId());
		Collection<ClassDictionary> classes = classService.getAllClasses();
		int classId = classService.getClassIdByName(characterRequest.getClassName());
		Collection<SkillDictionary> skills = classService.getAllSkillsByClassId(classId);
		SendMessage classDescriptionMessage = elementFactory.classConfirmationMessage(characterRequest, skills, classes);
		execute(classDescriptionMessage);
		log.debug("Character class description has been sent successfully");
	}

	public void sendRegistrationRequest(CharacterRequest characterRequest) {
		log.debug("Sending registration request to the user [{}]...", characterRequest.getUserId());
		SendMessage registrationRequestMessage = elementFactory.registrationConfirmationMessage(characterRequest);
		execute(registrationRequestMessage);
		log.debug("Character registration request has been sent successfully");
	}

	public void sendRegistrationConfirmation(ActionContext actionContext) {
		log.debug("Sending isSuccessful registration message to the user [{}]...", actionContext.userId());
		SendMessage successFullRegistrationMessage = elementFactory.successFullRegistrationMessage(actionContext.userId());
		execute(successFullRegistrationMessage);
		log.debug("Character registration confirmation message has been sent successfully");
	}

	public void resendCharacterNameRequest(CharacterRequest characterRequest) {
		log.debug("Resending character name request to user [{}] due to invalid name received.", characterRequest.getUserId());
		SendMessage nameMessage = elementFactory.illegalCharacterNameMessage(characterRequest);
		execute(nameMessage);
		log.debug("Character name request for invalid name sent successfully.");
	}
}
