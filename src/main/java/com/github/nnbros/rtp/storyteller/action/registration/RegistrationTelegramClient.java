package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuTelegramClient;
import com.github.nnbros.rtp.storyteller.character.CharacterParameter;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.SkillDictionary;
import com.github.nnbros.rtp.storyteller.character.ClassService;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import com.github.nnbros.rtp.storyteller.telegram.AbstractTelegramClient;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.*;

import static com.github.nnbros.rtp.storyteller.action.registration.RegistrationElement.*;

@Slf4j
@Service
public class RegistrationTelegramClient extends AbstractTelegramClient {
	public static final String DESCRIPTION_TEMPLATE = "%s: %s\n";

	private final ClassService classService;
	private final MainMenuTelegramClient mainMenuTelegramClient;
	private final Localization localization;

	public RegistrationTelegramClient(TelegramClient telegramClient,
									  ClassService classService,
									  TelegramElementRegistry elementRegistry,
									  MainMenuTelegramClient mainMenuTelegramClient, Localization localization) {
		super(telegramClient, elementRegistry);
		this.classService = classService;
		this.mainMenuTelegramClient = mainMenuTelegramClient;
		this.localization = localization;
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
		Map<String, Localization.Clazz> localizedClasses = localization.getClasses();
		classes.stream()
				.map(ClassDictionary::name)
				.map(localizedClasses::get)
				.forEach(clazz ->
						classesDescription.append(DESCRIPTION_TEMPLATE.formatted(clazz.getName(), clazz.getDescription())));
		Parameter classesDescriptionParameter = Parameter.of(RegistrationParameter.CLASSES_DESCRIPTION, classesDescription);

		//TODO remove after dynamic params are implemented
		String warriorLocalizedClassName = localizedClasses.get(RegistrationParameter.WARRIOR.getKey()).getName();
		Parameter warriorClassParameter = Parameter.of(RegistrationParameter.WARRIOR_CLASS_TEXT, warriorLocalizedClassName);
		String rogueLocalizedClassName = localizedClasses.get(RegistrationParameter.ROGUE.getKey()).getName();
		Parameter rogueClassParameter = Parameter.of(RegistrationParameter.ROGUE_CLASS_TEXT, rogueLocalizedClassName);

		Map<String, String> parameters = buildRegistrationParameters(
				characterRequest,
				classesDescriptionParameter,
				warriorClassParameter,
				rogueClassParameter);

		BotApiMethod<?> classOptionsMessage = buildBotApiMethod(charClassSelection, parameters);
		execute(classOptionsMessage);
		log.debug("Class options have been sent successfully");
	}

	public void sendClassDescription(CharacterRequest characterRequest) {
		log.debug("Sending character class description to the user [{}]...", characterRequest.getUserId());
		String className = characterRequest.getClassName();
		String confirmationEmojiText = buildText(confirmationEmoji);

		//TODO remove after dynamic params are implemented
		Map<String, Localization.Clazz> localizedClasses = localization.getClasses();
		String warriorLocalizedClassName = localizedClasses.get(RegistrationParameter.WARRIOR.getKey()).getName();
		Parameter warriorClassParameter = Parameter.of(RegistrationParameter.WARRIOR_CLASS_TEXT, warriorLocalizedClassName);
		String rogueLocalizedClassName = localizedClasses.get(RegistrationParameter.ROGUE.getKey()).getName();
		Parameter rogueClassParameter = Parameter.of(RegistrationParameter.ROGUE_CLASS_TEXT, rogueLocalizedClassName);
		String localizedClassName = localizedClasses.get(className).getName();
		String confirmedClassButtonText = CONFIRMED_OPTION_TEMPLATE.formatted(confirmationEmojiText, localizedClassName);
		if (className.equals(RegistrationParameter.WARRIOR.getKey())) {
			warriorClassParameter = Parameter.of(RegistrationParameter.WARRIOR_CLASS_TEXT, confirmedClassButtonText);
		} else {
			rogueClassParameter = Parameter.of(RegistrationParameter.ROGUE_CLASS_TEXT, confirmedClassButtonText);
		}

		//TODO caching
		Collection<SkillDictionary> skills = classService.getAllSkillsByClassName(className);
		StringBuilder classDescriptionsText = new StringBuilder();
		skills.stream()
				.map(SkillDictionary::name)
				.map(skill -> localization.getSkills().get(skill))
				.forEach(skill -> classDescriptionsText.append(DESCRIPTION_TEMPLATE.formatted(skill.getName(), skill.getDescription())));
		Parameter classesDescriptionParameter = Parameter.of(RegistrationParameter.SKILLS_DESCRIPTION, classDescriptionsText);
		Map<String, String> parameters = buildRegistrationParameters(characterRequest, classesDescriptionParameter, warriorClassParameter, rogueClassParameter);

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
		Map<String, String> params = Parameters.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, actionContext.userId()),
				Parameter.of(DefaultParameter.MESSAGE_ID, actionContext.messageId())
		);
		BotApiMethod<?> registrationRequestMessage = buildBotApiMethod(registrationComplete, params);
		execute(registrationRequestMessage);
		mainMenuTelegramClient.sendMainMenu(actionContext);
		log.debug("Character registration confirmation message has been sent successfully");
	}

	public void resendCharacterNameRequest(CharacterRequest characterRequest) {
		log.debug("Resending character name request to user [{}] due to invalid name received.", characterRequest.getUserId());
		String gender = buildGender(characterRequest);
		Map<String, String> parameters = Parameters.buildParameters(
				Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()),
				Parameter.of(CharacterParameter.CHAR_GENDER, gender)
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
			parameters.add(Parameter.of(CharacterParameter.CHAR_GENDER, buildGender(characterRequest)));
		}
		if (Objects.nonNull(characterRequest.getName())) {
			parameters.add(Parameter.of(CharacterParameter.CHAR_NAME, characterRequest.getName()));
		}
		String className = characterRequest.getClassName();
		if (Objects.nonNull(className)) {
			String localizedClassName = localization.getClasses()
					.get(className)
					.getName();
			parameters.add(Parameter.of(CharacterParameter.CHAR_CLASS, localizedClassName));
		}
		parameters.addAll(Arrays.asList(additionalParams));
		return Parameters.buildParameters(parameters);
	}

	private String buildGender(CharacterRequest characterRequest) {
		return buildText(REGISTRATION_GROUP_NAME, characterRequest.getGender().getElementName(), Collections.emptyMap());
	}
}
