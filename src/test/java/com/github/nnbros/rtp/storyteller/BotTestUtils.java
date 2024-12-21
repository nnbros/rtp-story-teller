package com.github.nnbros.rtp.storyteller;

import com.github.nnbros.rtp.storyteller.action.ActionContext;
import com.github.nnbros.rtp.storyteller.action.registration.CharacterRequest;
import com.github.nnbros.rtp.storyteller.character.Character;
import com.github.nnbros.rtp.storyteller.character.CharacterClass;
import com.github.nnbros.rtp.storyteller.character.ClassDictionary;
import com.github.nnbros.rtp.storyteller.character.Gender;
import com.github.nnbros.rtp.storyteller.telegram.UpdateType;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

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
	public static final String TEST_CLASS_NAME_1 = "testClassName1";
	public static final String TEST_CLASS_NAME_2 = "testClassName2";
	public static final String TEST_DESCRIPTION = "testDescription1";

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
		return new ActionContext(TEST_ACTION_NAME, TEST_USER_ID, UpdateType.CALLBACK_QUERY, createTestEmptyUpdate(), TEST_MESSAGE_ID, TEST_ACTION_DATA);
	}

	public static CharacterRequest createTestCharacterRequest() {
		CharacterRequest characterRequest = new CharacterRequest(TEST_USER_ID);
		characterRequest.setName(TEST_CHARACTER_NAME);
		characterRequest.setGender(Gender.MALE);
		characterRequest.setClassName(TEST_CLASS_NAME_1);
		characterRequest.setLastMessageId(TEST_MESSAGE_ID);
		return characterRequest;
	}

	public static ClassDictionary createTestClassDictionary() {
		return createTestClassDictionary(TEST_CLASS_NAME_1);
	}

	public static ClassDictionary createTestClassDictionary(String className) {
		return new ClassDictionary(1, className, TEST_DESCRIPTION, 1, 1, 1);
	}

	public static List<CharacterClass> createTestCharClasses() {
		return List.of(
				new CharacterClass(createTestClassDictionary(TEST_CLASS_NAME_1), 0L),
				new CharacterClass(createTestClassDictionary(TEST_CLASS_NAME_2), 0L)
		);
	}

	public static Character createTestCharacter() {
		return new Character(TEST_CHARACTER_NAME, Gender.MALE, createTestCharClasses(), createTestClassDictionary());
	}
}
