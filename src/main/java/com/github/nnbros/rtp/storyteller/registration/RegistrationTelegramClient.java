package com.github.nnbros.rtp.storyteller.registration;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.CharacterRequest;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.SkillDictionary;
import com.github.nnbros.rtp.storyteller.jpa.character.ClassService;
import com.github.nnbros.rtp.storyteller.telegram.AbstractTelegramClient;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultParameter;
import com.github.nnbros.rtp.storyteller.telegram.ui.Parameter;
import com.github.nnbros.rtp.storyteller.telegram.ui.ParametersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.*;

import static com.github.nnbros.rtp.storyteller.registration.RegistrationElement.*;

@Slf4j
@Service
public class RegistrationTelegramClient extends AbstractTelegramClient {
	public static final String DESCRIPTION_TEMPLATE = "%s: %s\n";
	public static final String CONFIRMED_CLASS_TEMPLATE = "%s%s";

	private final ClassService classService;
	private final ParametersBuilder parametersBuilder = new ParametersBuilder();

	public RegistrationTelegramClient(TelegramClient telegramClient, ClassService classService, TelegramElementRegistry elementRegistry) {
		super(telegramClient, elementRegistry);
		this.classService = classService;
	}

	public void sendGenderOptions(ActionContext actionContext) {
		log.debug("Sending character gender request to the user [{}]...", actionContext.userId());
		Map<String, String> params = Map.of(DefaultParameter.CHAT_ID.getKey(), actionContext.userId().toString());
		BotApiMethod<?> genderMessage = buildBotApiMethod(createCharGender, params);
		execute(genderMessage);
		log.debug("Character gender request has been sent successfully");
	}

	public void requestName(CharacterRequest characterRequest) {
		log.debug("Sending character name request to the user [{}]...", characterRequest.getUserId());
		Map<String, String> parameters = buildRegistrationParameters(characterRequest);
		BotApiMethod<?> nameMessage = buildBotApiMethod(createCharName, parameters);
		execute(nameMessage);
		log.debug("Character name request has been sent successfully");
	}

	public void sendClassOptions(CharacterRequest characterRequest) {
		log.debug("Sending character class options to the user [{}]...", characterRequest.getUserId());
		Collection<ClassDictionary> classes = classService.getAllClasses();
		StringBuilder classesDescription = new StringBuilder();
		classes.forEach(classDictionary ->
				classesDescription.append(DESCRIPTION_TEMPLATE.formatted(classDictionary.name(), classDictionary.description())));
		Parameter classesDescriptionParameter = Parameter.of(RegistrationParameter.CLASSES_DESCRIPTION, classesDescription);
		Map<String, String> parameters = buildRegistrationParameters(characterRequest, classesDescriptionParameter);

		BotApiMethod<?> classOptionsMessage = buildBotApiMethod(charClassSelection, parameters);
		execute(classOptionsMessage);
		log.debug("Class options have been sent successfully");
	}

	public void sendClassDescription(CharacterRequest characterRequest) {
		log.debug("Sending character class description to the user [{}]...", characterRequest.getUserId());
		String className = characterRequest.getClassName();
		String confirmationEmojiText = buildText(confirmationEmoji);

		//TODO remove after dynamic button generation is implemented and class description is moved to configs
		Parameter confirmedClass;
		String confirmedClassButtonText = CONFIRMED_CLASS_TEMPLATE.formatted(confirmationEmojiText, className);
		if (className.equals(CharClass.WARRIOR.getClassName())) {
			confirmedClass = Parameter.of(RegistrationParameter.CONFIRMATION_WARRIOR, confirmedClassButtonText);
		} else {
			confirmedClass = Parameter.of(RegistrationParameter.CONFIRMATION_ROUGE, confirmedClassButtonText);
		}

		int classId = classService.getClassIdByName(className);
		Collection<SkillDictionary> skills = classService.getAllSkillsByClassId(classId);
		StringBuilder classDescriptionsText = new StringBuilder();
		skills.forEach(skill -> classDescriptionsText.append(DESCRIPTION_TEMPLATE.formatted(skill.name(), skill.description())));
		Parameter classesDescriptionParameter = Parameter.of(RegistrationParameter.SKILLS_DESCRIPTION, classDescriptionsText);
		Map<String, String> parameters = buildRegistrationParameters(characterRequest, classesDescriptionParameter, confirmedClass);

		BotApiMethod<?> classDescriptionMessage = buildBotApiMethod(REGISTRATION_GROUP_NAME, charClassConfirmation.name(), parameters);
		execute(classDescriptionMessage);
		log.debug("Character class description has been sent successfully");
	}

	public void sendRegistrationRequest(CharacterRequest characterRequest) {
		log.debug("Sending registration request to the user [{}]...", characterRequest.getUserId());
		Map<String, String> parameters = buildRegistrationParameters(characterRequest);
		BotApiMethod<?> registrationRequestMessage = buildBotApiMethod(charRegistration, parameters);
		execute(registrationRequestMessage);
		log.debug("Character registration request has been sent successfully");
	}

	public void sendRegistrationConfirmation(ActionContext actionContext) {
		log.debug("Sending isSuccessful registration message to the user [{}]...", actionContext.userId());
		Map<String, String> params = parametersBuilder.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, actionContext.userId()),
				Parameter.of(DefaultParameter.MESSAGE_ID, actionContext.messageId())
		);
		BotApiMethod<?> registrationRequestMessage = buildBotApiMethod(registrationComplete, params);
		execute(registrationRequestMessage);
		log.debug("Character registration confirmation message has been sent successfully");
	}

	public void resendCharacterNameRequest(CharacterRequest characterRequest) {
		log.debug("Resending character name request to user [{}] due to invalid name received.", characterRequest.getUserId());
		String gender = buildGender(characterRequest);
		Map<String, String> parameters = parametersBuilder.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()),
				Parameter.of(RegistrationParameter.CHAR_GENDER, gender)
		);
		BotApiMethod<?> nameMessage = buildBotApiMethod(REGISTRATION_GROUP_NAME, invalidCharName.name(), parameters);

		execute(nameMessage);
		log.debug("Character name request for invalid name sent successfully.");
	}

	private Map<String, String> buildRegistrationParameters(CharacterRequest characterRequest, Parameter... additionalParams) {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()));
		parameters.add(Parameter.of(DefaultParameter.MESSAGE_ID, characterRequest.getLastMessageId()));
		if (Objects.nonNull(characterRequest.getGender())) {
			parameters.add(Parameter.of(RegistrationParameter.CHAR_GENDER, buildGender(characterRequest)));
		}
		if (Objects.nonNull(characterRequest.getName())) {
			parameters.add(Parameter.of(RegistrationParameter.CHAR_NAME, characterRequest.getName()));
		}
		if (Objects.nonNull(characterRequest.getClassName())) {
			parameters.add(Parameter.of(RegistrationParameter.CHAR_CLASS, characterRequest.getClassName()));
		}
		parameters.addAll(Arrays.asList(additionalParams));
		return parametersBuilder.buildParameters(parameters);
	}

	private String buildGender(CharacterRequest characterRequest) {
		return buildText(REGISTRATION_GROUP_NAME, characterRequest.getGender().getElementName(), Collections.emptyMap());
	}
}
