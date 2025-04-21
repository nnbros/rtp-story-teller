package com.github.nnbros.rtp.storyteller;

import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.guronas.telegram.bot.elements.parameter.Parameters;
import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.character.CharacterParameter;
import com.github.nnbros.rtp.storyteller.action.registration.CharacterRequest;
import com.github.nnbros.rtp.storyteller.character.*;
import com.github.nnbros.rtp.storyteller.configuration.Localization;
import com.github.nnbros.rtp.storyteller.telegram.UpdateType;
import com.github.nnbros.rtp.storyteller.telegram.ui.DefaultParameter;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.nnbros.rtp.storyteller.character.Archetype.*;

public class BotTestUtils {
	public static final String TEST_ACTION_NAME = "test_action";
	public static final String TEST_ACTION_DATA = "test_action_data";
	public static final int TEST_UPDATE_ID = 456;
	public static final String TEST_TEXT = "util";
	public static final long TEST_USER_ID = 123L;
	public static final int TEST_MESSAGE_ID = 12345;
	public static final String CHAT_PRIVATE_TYPE = "private";
	public static final String COMMAND_MESSAGE_TYPE = "bot_command";
	public static final String TEST_CALLBACK_DATA = "test_data";
	public static final String TEST_CHARACTER_NAME = "testCharacterName";

	public static final String TEST_DESCRIPTION_1 = "testDescription1";
	public static final String TEST_DESCRIPTION_2 = "testDescription2";
	public static final String TEST_DESCRIPTION_3 = "testDescription3";
	public static final String TEST_DESCRIPTION_4 = "testDescription4";
	public static final String TEST_DESCRIPTION_5 = "testDescription5";
	public static final String TEST_DESCRIPTION_6 = "testDescription6";

	public static final String TEST_CLASS_1 = "warrior";
	public static final String TEST_CLASS_2 = "rogue";
	public static final String TEST_CLASS_3 = "testClass3";
	public static final String TEST_CLASS_NAME_1 = "testClassName1";
	public static final String TEST_CLASS_NAME_2 = "testClassName2";
	public static final String TEST_CLASS_NAME_3 = "testClassName3";
	public static final ClassDictionary TEST_CLASS_POJO_1 = new ClassDictionary(1, SWORDSMAN, TEST_CLASS_1, 1, 1, 1, 0.25f);
	public static final ClassDictionary TEST_CLASS_POJO_2 = new ClassDictionary(2, NEUTRAL, TEST_CLASS_2, 2, 2, 2, 0.25f);
	public static final ClassDictionary TEST_CLASS_POJO_3 = new ClassDictionary(3, SPEARMAN, TEST_CLASS_3, 3, 3, 3, 0.25f);
	public static final ActiveCharacterClass TEST_ACTIVE_CLASS_POJO_1 = new ActiveCharacterClass(1, 1, 1, SWORDSMAN, TEST_CLASS_1, 1, 1, 1, 0.25f, 0L);
	public static final ActiveCharacterClass TEST_ACTIVE_CLASS_POJO_2 = new ActiveCharacterClass(2, 2, 2, NEUTRAL, TEST_CLASS_2, 2, 2, 2, 0.25f, 0L);
	public static final ActiveCharacterClass TEST_ACTIVE_CLASS_POJO_3 = new ActiveCharacterClass(3, 3, 3, SPEARMAN, TEST_CLASS_3, 3, 3, 3, 0.25f, 0L);

	public static final String TEST_SKILL_1 = "testSkill1";
	public static final String TEST_SKILL_2 = "testSkill2";
	public static final String TEST_SKILL_3 = "testSkill3";
	public static final String TEST_SKILL_NAME_1 = "testSkillName1";
	public static final String TEST_SKILL_NAME_2 = "testSkillName2";
	public static final String TEST_SKILL_NAME_3 = "testSkillName3";
	public static final SkillDictionary TEST_SKILL_POJO_1 = new SkillDictionary(TEST_SKILL_1, SkillType.BASIC_CHARACTER, SWORDSMAN);
	public static final SkillDictionary TEST_SKILL_POJO_2 = new SkillDictionary(TEST_SKILL_2, SkillType.BASIC_CHARACTER, NEUTRAL);
	public static final SkillDictionary TEST_SKILL_POJO_3 = new SkillDictionary(TEST_SKILL_3, SkillType.BASIC_CHARACTER, SPEARMAN);

	public static final String TEST_ARMY_1 = "seekers";
	public static final String TEST_ARMY_2 = "scouts";
	public static final String TEST_ARMY_3 = "peasants";
	public static final String TEST_ARMY_NAME_1 = "testArmyName1";
	public static final String TEST_ARMY_NAME_2 = "testArmyName2";
	public static final String TEST_ARMY_NAME_3 = "testArmyName3";
	public static final String TEST_ARMY_TYPE_1 = "testArmyType1";
	public static final String TEST_ARMY_TYPE_2 = "testArmyType2";
	public static final String TEST_ARMY_TYPE_3 = "testArmyType3";
	public static final ArmyDictionary TEST_ARMY_POJO_1 = new ArmyDictionary(1, TEST_ARMY_1, SWORDSMAN, 1, 1, 1, 1, 0.25f);
	public static final ArmyDictionary TEST_ARMY_POJO_2 = new ArmyDictionary(2, TEST_ARMY_2, CAVALRY, 2, 2, 2, 2, 0.25f);
	public static final ArmyDictionary TEST_ARMY_POJO_3 = new ArmyDictionary(3, TEST_ARMY_3, SPEARMAN, 3, 3, 3, 3, 0.25f);
	public static final ActiveCharacterArmy TEST_ACTIVE_ARMY_POJO_1 = new ActiveCharacterArmy(1, 1, 1, TEST_ARMY_1, SWORDSMAN, 1, 1, 1, 1, 0.25f, 1, 1);
	public static final ActiveCharacterArmy TEST_ACTIVE_ARMY_POJO_2 = new ActiveCharacterArmy(2, 1, 1, TEST_ARMY_2, CAVALRY, 2, 2, 2, 2, 0.25f, 1, 1);
	public static final ActiveCharacterArmy TEST_ACTIVE_ARMY_POJO_3 = new ActiveCharacterArmy(3, 1, 1, TEST_ARMY_3, SPEARMAN, 3, 3, 3, 3, 0.25f, 1, 1);

	public static final String TEST_CONFIRMATION_EMOJI = ":)";
	public static final String TEST_CONFIRMATION_CLASS_PARAM = TEST_CONFIRMATION_EMOJI + TEST_CLASS_NAME_1;
	public static final String TEST_CONFIRMATION_ARMY_PARAM = TEST_CONFIRMATION_EMOJI + TEST_ARMY_NAME_1;

	public static User createTestUser() {
		return User.builder()
				.id(TEST_USER_ID)
				.firstName("TestUser")
				.isBot(false)
				.build();
	}

	public static Message createTestMessage() {
		return createTestMessage(TEST_TEXT);
	}

	public static Message createTestMessage(String text) {
		Chat chat = Chat.builder()
				.type(CHAT_PRIVATE_TYPE)
				.id(TEST_USER_ID)
				.build();
		User from = createTestUser();
		return Message.builder()
				.text(text)
				.chat(chat)
				.from(from)
				.messageId(TEST_MESSAGE_ID)
				.build();
	}

	public static Message createTestCommandMessage(String text) {
		MessageEntity messageEntity = MessageEntity.builder()
				.length(1)
				.offset(0)
				.type(COMMAND_MESSAGE_TYPE)
				.build();
		Message message = createTestMessage(text);
		message.setEntities(List.of(messageEntity));
		return message;
	}

	public static CallbackQuery createTestCallbackQuery(String data) {
		CallbackQuery callbackQuery = new CallbackQuery();
		callbackQuery.setFrom(createTestUser());
		callbackQuery.setData(data);
		callbackQuery.setMessage(createTestMessage());
		return callbackQuery;
	}

	public static Update createTestEmptyUpdate() {
		Update update = new Update();
		update.setUpdateId(TEST_UPDATE_ID);
		return update;
	}

	public static Update createTestMessageUpdate() {
		Update update = new Update();
		update.setMessage(createTestMessage());
		update.setUpdateId(TEST_UPDATE_ID);
		return update;
	}

	public static Update createTestMessageUpdate(Message message) {
		Update update = createTestMessageUpdate();
		update.setMessage(message);
		return update;
	}

	public static Update createTestEditedMessageUpdate() {
		return createTestEditedMessageUpdate(createTestMessage());
	}

	public static Update createTestEditedMessageUpdate(Message message) {
		Update update = new Update();
		update.setEditedMessage(message);
		update.setUpdateId(TEST_UPDATE_ID);
		return update;
	}

	public static Update createTestCallbackQueryUpdate() {
		return createTestCallbackQueryUpdate(TEST_CALLBACK_DATA);
	}

	public static Update createTestCallbackQueryUpdate(String data) {
		Update update = new Update();
		update.setCallbackQuery(createTestCallbackQuery(data));
		update.setUpdateId(TEST_UPDATE_ID);
		return update;
	}

	public static ActionContext createTestActionContext() {
		return createTestActionContext(TEST_ACTION_DATA);
	}

	public static ActionContext createTestActionContext(String data) {
		return new ActionContext(TEST_ACTION_NAME, TEST_USER_ID, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, data);
	}

	public static CharacterRequest createTestCharacterRequest() {
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		characterRequest.setName(TEST_CHARACTER_NAME);
		characterRequest.setGender(Gender.MALE);
		characterRequest.setClassName(TEST_CLASS_1);
		characterRequest.setLastMessageId(TEST_MESSAGE_ID);
		return characterRequest;
	}

	public static ClassDictionary createTestClassDictionary() {
		return createTestClassDictionary(TEST_CLASS_1);
	}

	public static ClassDictionary createTestClassDictionary(String className) {
		return new ClassDictionary(1, SWORDSMAN, className, 1, 1, 1, 0.25f);
	}

	public static List<CharacterClass> createTestCharClasses() {
		return List.of(
				new CharacterClass(createTestClassDictionary(TEST_CLASS_1), 0L),
				new CharacterClass(createTestClassDictionary(TEST_CLASS_2), 0L)
		);
	}

	public static DetailedCharacterView createTestDetailedCharacterView() {
		DetailedCharacterView detailedCharacterView = new DetailedCharacterView();
		detailedCharacterView.setName(TEST_CHARACTER_NAME);
		detailedCharacterView.setActiveClass(TEST_ACTIVE_CLASS_POJO_1);
		detailedCharacterView.setActiveArmy(TEST_ACTIVE_ARMY_POJO_1);
		return detailedCharacterView;
	}

	public static Localization createTestLocalization() {
		Localization localization = new Localization();
		localization.setClasses(
				Map.of(
						TEST_CLASS_1, new Localization.Clazz(TEST_CLASS_NAME_1, TEST_DESCRIPTION_1),
						TEST_CLASS_2, new Localization.Clazz(TEST_CLASS_NAME_2, TEST_DESCRIPTION_2),
						TEST_CLASS_3, new Localization.Clazz(TEST_CLASS_NAME_3, TEST_DESCRIPTION_3)
				)
		);
		localization.setSkills(
				Map.of(
						TEST_SKILL_1, new Localization.Skill(TEST_SKILL_NAME_1, TEST_DESCRIPTION_4),
						TEST_SKILL_2, new Localization.Skill(TEST_SKILL_NAME_2, TEST_DESCRIPTION_5),
						TEST_SKILL_3, new Localization.Skill(TEST_SKILL_NAME_3, TEST_DESCRIPTION_6)
				)
		);
		localization.setArmies(
				Map.of(
						TEST_ARMY_1, new Localization.Army(TEST_ARMY_NAME_1, TEST_DESCRIPTION_4, TEST_ARMY_TYPE_1),
						TEST_ARMY_2, new Localization.Army(TEST_ARMY_NAME_2, TEST_DESCRIPTION_5, TEST_ARMY_TYPE_2),
						TEST_ARMY_3, new Localization.Army(TEST_ARMY_NAME_3, TEST_DESCRIPTION_6, TEST_ARMY_TYPE_3)
				)
		);
		return localization;
	}

	public static Map<String, String> createTestCharacterBaseParameters(Parameter... additionalParams) {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(Parameter.of(DefaultParameter.CHAT_ID, TEST_USER_ID));
		parameters.add(Parameter.of(CharacterParameter.CHAR_NAME, TEST_CHARACTER_NAME));
		parameters.add(Parameter.of(CharacterParameter.CHAR_CLASS, TEST_CLASS_NAME_1));
		parameters.add(Parameter.of(CharacterParameter.CHAR_CLASS_DESCRIPTION, TEST_DESCRIPTION_1));
		parameters.add(Parameter.of(CharacterParameter.CHAR_ARMY, TEST_ARMY_NAME_1));
		parameters.add(Parameter.of(CharacterParameter.CHAR_ARMY_TYPE, TEST_ARMY_TYPE_1));
		parameters.add(Parameter.of(CharacterParameter.CHAR_ARMY_HP, 1));
		parameters.add(Parameter.of(CharacterParameter.CHAR_ARMY_COUNT, 1));
		parameters.add(Parameter.of(CharacterParameter.CHAR_ARMY_ATK, 1));
		parameters.add(Parameter.of(CharacterParameter.CHAR_ARMY_DEF, 1));
		parameters.addAll(Arrays.asList(additionalParams));
		return Parameters.buildParameters(parameters);
	}
}
