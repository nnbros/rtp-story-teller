package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.parameter.InlineKeyboardButtonParameters;
import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.common.action.ActionContext;
import com.github.nnbros.rtp.common.action.ActionResult;
import com.github.nnbros.rtp.common.telegram.AbstractTelegramClient;
import com.github.nnbros.rtp.common.telegram.ui.CharacterParameter;
import com.github.nnbros.rtp.common.telegram.ui.DefaultParameter;
import com.github.nnbros.rtp.storyteller.action.mainmenu.MainMenuTelegramClient;
import com.github.nnbros.rtp.storyteller.character.*;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.*;

import static com.github.nnbros.rtp.storyteller.action.registration.CharacterAction.CLASS_SELECTION;
import static com.github.nnbros.rtp.storyteller.action.registration.RegistrationElement.*;

@Slf4j
@Service
public class RegistrationTelegramClient extends AbstractTelegramClient {
	public static final String DESCRIPTION_TEMPLATE = "%s: %s\n";

	private final ClassService classService;
	private final SkillService skillService;
	private final MainMenuTelegramClient mainMenuTelegramClient;
	private final Localization localization;

	public RegistrationTelegramClient(TelegramClient telegramClient,
									  ClassService classService,
									  TelegramElementRegistry elementRegistry, SkillService skillService,
									  MainMenuTelegramClient mainMenuTelegramClient, Localization localization) {
		super(telegramClient, elementRegistry);
		this.classService = classService;
		this.skillService = skillService;
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

	public void requestName(ActionResult<CharacterRequest> actionResult) {
		CharacterRequest characterRequest = actionResult.getValue();
		Objects.requireNonNull(characterRequest);
		log.debug("Sending character name request to the user [{}]...", characterRequest.getUserId());
		Map<String, String> parameters = buildRegistrationParameters(actionResult.getActionContext().messageId(), characterRequest);
		BotApiMethod<?> nameMessage = buildBotApiMethod(createCharName, parameters);
		execute(nameMessage);
		log.debug("Character name request has been sent successfully");
	}

	public void sendClassOptions(ActionResult<CharacterRequest> actionResult) {
		CharacterRequest characterRequest = actionResult.getValue();
		Objects.requireNonNull(characterRequest);
		log.debug("Sending character class options to the user [{}]...", characterRequest.getUserId());
		Collection<ClassDictionary> classes = classService.getAllClasses();
		StringBuilder classesDescription = new StringBuilder();
		InlineKeyboardButtonParameters dynamicParameters = new InlineKeyboardButtonParameters();
		classes.stream()
				.map(ClassDictionary::name)
				.forEach(className -> addClassParameters(className, dynamicParameters, classesDescription));

		Parameter classesDescriptionParameter = Parameter.of(RegistrationParameter.CLASSES_DESCRIPTION, classesDescription);
		ActionContext actionContext = actionResult.getActionContext();
		Map<String, String> parameters = buildRegistrationParameters(actionContext.messageId(), characterRequest, classesDescriptionParameter);

		BotApiMethod<?> classOptionsMessage = buildBotApiMethod(charClassSelection,
				parameters,
				Map.of(RegistrationParameter.CLASSES.getKey(), dynamicParameters));
		execute(classOptionsMessage);
		log.debug("Class options have been sent successfully");
	}

	public void sendClassDescription(ActionResult<CharacterRequest> actionResult) {
		CharacterRequest characterRequest = actionResult.getValue();
		Objects.requireNonNull(characterRequest);
		log.debug("Sending character class description to the user [{}]...", characterRequest.getUserId());
		String selectedClass = characterRequest.getClassName();
		String confirmationEmojiText = buildText(confirmationEmoji);

		InlineKeyboardButtonParameters dynamicParameters = new InlineKeyboardButtonParameters();
		classService.getAllClasses()
				.stream()
				.map(ClassDictionary::name)
				.forEach(className -> addClassDynamicParameters(dynamicParameters, className, className.equals(selectedClass), confirmationEmojiText));

		//TODO caching
		Collection<SkillDictionary> skills = skillService.getAllSkillsByClassName(selectedClass);
		StringBuilder classDescriptionsText = new StringBuilder();
		skills.stream()
				.map(SkillDictionary::name)
				.map(skill -> localization.getSkills().get(skill))
				.forEach(skill -> classDescriptionsText.append(DESCRIPTION_TEMPLATE.formatted(skill.getName(), skill.getDescription())));
		Parameter classesDescriptionParameter = Parameter.of(RegistrationParameter.SKILLS_DESCRIPTION, classDescriptionsText);
		ActionContext actionContext = actionResult.getActionContext();
		Map<String, String> parameters = buildRegistrationParameters(actionContext.messageId(), characterRequest, classesDescriptionParameter);

		BotApiMethod<?> classDescriptionMessage = buildBotApiMethod(REGISTRATION_GROUP_NAME,
				charClassConfirmation.name(),
				parameters,
				Map.of(RegistrationParameter.CLASSES.getKey(), dynamicParameters));
		execute(classDescriptionMessage);
		log.debug("Character class description has been sent successfully");
	}

	public void sendRegistrationRequest(ActionResult<CharacterRequest> actionResult) {
		CharacterRequest characterRequest = actionResult.getValue();
		Objects.requireNonNull(characterRequest);
		log.debug("Sending registration request to the user [{}]...", characterRequest.getUserId());
		Map<String, String> parameters = buildRegistrationParameters(actionResult.getActionContext().messageId(), characterRequest);
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

	public void resendCharacterNameRequest(ActionResult<CharacterRequest> actionResult) {
		CharacterRequest characterRequest = actionResult.getValue();
		Objects.requireNonNull(characterRequest);
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

	private Map<String, String> buildRegistrationParameters(Integer lastMessageId, CharacterRequest characterRequest, Parameter... additionalParams) {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(Parameter.of(DefaultParameter.CHAT_ID, characterRequest.getUserId()));
		parameters.add(Parameter.of(DefaultParameter.MESSAGE_ID, lastMessageId));
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

	private void addClassParameters(String className, InlineKeyboardButtonParameters dynamicParameters, StringBuilder classesDescription) {
		Localization.Clazz clazz = localization.getClasses().get(className);
		String localizedClassName = clazz.getName();
		String classDescription = clazz.getDescription();
		addClassDynamicParameters(dynamicParameters, className, localizedClassName);
		classesDescription.append(DESCRIPTION_TEMPLATE.formatted(localizedClassName, classDescription));
	}

	private void addClassDynamicParameters(InlineKeyboardButtonParameters parameters,
										   String className,
										   boolean isClassActive,
										   String confirmationEmoji) {
		String localizedClassName = localization.getClasses()
				.get(className)
				.getName();
		String finalText = isClassActive ? CONFIRMED_OPTION_TEMPLATE.formatted(confirmationEmoji, localizedClassName) : localizedClassName;
		addClassDynamicParameters(parameters, className, finalText);
	}

	private void addClassDynamicParameters(InlineKeyboardButtonParameters parameters,
										   String className,
										   String finalText) {
		parameters.add(finalText, CALLBACK_DATA_TEMPLATE.formatted(CLASS_SELECTION.getActionName(), className));
	}
}
