package com.github.nnbros.rtp.storyteller.telegram.ui;

import com.github.nnbros.rtp.storyteller.character.CharacterRequest;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.Gender;
import com.github.nnbros.rtp.storyteller.character.SkillDictionary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.github.nnbros.rtp.storyteller.telegram.ui.TelegramElements.*;

@Component
public class RegistrationTelegramElementFactory {
	public static final String REGISTRATION_MESSAGE_TEMPLATE = "%s\n%s";
	public static final String ILLEGAL_NAME_MESSAGE_TEMPLATE = "%s\n%s\n\n%s";
	public static final int MAX_CLASS_BUTTONS_IN_ROW = 2;
	public static final int MAX_REGISTRATION_BUTTONS_IN_ROW = 1;

	@Value("${ui.registration_messages.character.gender.male}")
	private String genderMaleText;
	@Value("${ui.registration_messages.character.gender.female}")
	private String genderFemaleText;
	@Value("${ui.registration_buttons.character.gender.male.id}")
	private String genderMaleButtonId;
	@Value("${ui.registration_buttons.character.gender.female.id}")
	private String genderFemaleButtonId;
	@Value("${ui.registration_messages.character.gender.request}")
	private String genderMessageText;
	@Value("${ui.registration_messages.character.gender.summary}")
	private String genderSummaryText;
	@Value("${ui.registration_messages.character.name_request}")
	private String characterNameMessageText;
	@Value("${ui.registration_messages.character.name_summary}")
	private String characterNameSummaryText;
	@Value("${ui.registration_messages.character.non_valid_name}")
	private String illegalCharacterNameMessageText;
	@Value("${ui.registration_messages.character.class_request}")
	private String classMessageText;
	@Value("${ui.registration_messages.character.class_short_description_template}")
	private String classShortDescription;
	@Value("${ui.registration_messages.character.class_full_description_template}")
	private String classFullDescription;
	@Value("${ui.registration_messages.character.class_summary}")
	private String classSummaryText;
	@Value("${ui.registration_buttons.character.class.id}")
	private String classButtonIdTemplate;
	@Value("${ui.registration_buttons.character.class.confirmation.emoji}")
	private String classConfirmationEmoji;
	@Value("${ui.registration_buttons.character.class.confirmation.button.text}")
	private String classConfirmationButtonText;
	@Value("${ui.registration_buttons.character.class.confirmation.button.id}")
	private String classConfirmationButtonId;
	@Value("${ui.registration_buttons.text}")
	private String registrationButtonText;
	@Value("${ui.registration_buttons.id}")
	private String registrationButtonId;
	@Value("${ui.registration_buttons.cancel.text}")
	private String cancelButtonText;
	@Value("${ui.registration_buttons.cancel.id}")
	private String cancelButtonId;
	@Value("${ui.registration_messages.registration_request}")
	private String registrationMessage;
	@Value("${ui.registration_messages.success}")
	private String successfulRegistrationMessage;

	public SendMessage characterGenderMessage(long userId) {
		InlineKeyboardButton genderMaleButton = inlineKeyboardButton(genderMaleText, genderMaleButtonId);
		InlineKeyboardButton genderFemaleButton = inlineKeyboardButton(genderFemaleText, genderFemaleButtonId);
		List<InlineKeyboardRow> keyboardRow = Collections.singletonList(new InlineKeyboardRow(genderMaleButton, genderFemaleButton));
		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(keyboardRow);
		return message(userId, genderMessageText, keyboardMarkup);
	}

	public SendMessage characterNameMessage(CharacterRequest characterRequest) {
		String summary = createSummary(characterRequest);
		String characterNameText = String.format(REGISTRATION_MESSAGE_TEMPLATE, summary, characterNameMessageText);
		return message(characterRequest.getUserId(), characterNameText);
	}

	public SendMessage illegalCharacterNameMessage(CharacterRequest characterRequest) {
		String summary = createSummary(characterRequest);
		String characterNameText = String.format(ILLEGAL_NAME_MESSAGE_TEMPLATE, summary, illegalCharacterNameMessageText, characterNameMessageText);
		return message(characterRequest.getUserId(), characterNameText);
	}

	public SendMessage classSelectionMessage(CharacterRequest characterRequest, Collection<ClassDictionary> classes) {
		String summary = createSummary(characterRequest);
		StringBuilder classDescriptionsText = new StringBuilder(classMessageText);
		classDescriptionsText.append(NEW_LINE);
		classes.forEach(classDictionary -> classDescriptionsText.append(classShortDescription.formatted(classDictionary.name(), classDictionary.description())));
		String classMessageText = String.format(REGISTRATION_MESSAGE_TEMPLATE, summary, classDescriptionsText);

		List<InlineKeyboardButton> buttons = classes.stream()
				.map(ClassDictionary::name)
				.map(className -> inlineKeyboardButton(className, classButtonIdTemplate.formatted(className)))
				.toList();
		List<InlineKeyboardRow> inlineKeyboardRows = inlineKeyboard(MAX_CLASS_BUTTONS_IN_ROW, buttons);

		return message(characterRequest.getUserId(), classMessageText, new InlineKeyboardMarkup(inlineKeyboardRows));
	}

	public SendMessage classConfirmationMessage(CharacterRequest characterRequest,
												Collection<SkillDictionary> skills,
												Collection<ClassDictionary> classes) {
		String summary = createSummary(characterRequest);
		StringBuilder classDescriptionsText = new StringBuilder();
		skills.forEach(skill -> classDescriptionsText.append(classShortDescription.formatted(skill.name(), skill.description())));
		String classMessageText = String.format(REGISTRATION_MESSAGE_TEMPLATE, summary, classFullDescription.formatted(classDescriptionsText.toString()));

		List<InlineKeyboardButton> buttons = classes.stream()
				.map(ClassDictionary::name)
				.map(className -> characterRequest.getClassName().equals(className) ? classConfirmationEmoji + className : className)
				.map(className -> inlineKeyboardButton(className, classButtonIdTemplate.formatted(className)))
				.toList();
		List<InlineKeyboardRow> inlineKeyboardRows = inlineKeyboard(MAX_CLASS_BUTTONS_IN_ROW, buttons);
		InlineKeyboardButton classConfirmationButton = inlineKeyboardButton(classConfirmationButtonText, classConfirmationButtonId);
		inlineKeyboardRows.add(new InlineKeyboardRow(classConfirmationButton));

		return message(characterRequest.getUserId(), classMessageText, new InlineKeyboardMarkup(inlineKeyboardRows));
	}

	public SendMessage registrationConfirmationMessage(CharacterRequest characterRequest) {
		String summary = createSummary(characterRequest);
		String registrationMessageText = String.format(REGISTRATION_MESSAGE_TEMPLATE, summary, registrationMessage);

		InlineKeyboardButton registrationButton = inlineKeyboardButton(registrationButtonText, registrationButtonId);
		InlineKeyboardButton cancelButton = inlineKeyboardButton(cancelButtonText, cancelButtonId);
		List<InlineKeyboardRow> keyboardRows = inlineKeyboard(MAX_REGISTRATION_BUTTONS_IN_ROW, List.of(registrationButton, cancelButton));

		return message(characterRequest.getUserId(), registrationMessageText, new InlineKeyboardMarkup(keyboardRows));
	}

	public SendMessage successFullRegistrationMessage(long userId) {
		return message(userId, successfulRegistrationMessage);
	}

	private String createSummary(CharacterRequest characterRequest) {
		StringBuilder summary = new StringBuilder();
		appendSummaryBlock(summary, characterRequest.getGender() == Gender.MALE ? genderMaleText : genderFemaleText, genderSummaryText);
		appendSummaryBlock(summary, characterRequest.getName(), characterNameSummaryText);
		appendSummaryBlock(summary, characterRequest.getClassName(), classSummaryText);
		return summary.toString();
	}

	private void appendSummaryBlock(StringBuilder summary, String value, String template) {
		if (value != null) {
			summary.append(template.formatted(value));
			summary.append(NEW_LINE);
		}
	}
}
