package com.github.nnbros.rtp.storyteller.telegram.ui;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class TelegramElements {
	public static final String NEW_LINE = "\n";

	public static SendMessage message(long userId, String text) {
		return messageBuilder(userId, text).build();
	}

	public static SendMessage message(long userId, String text, InlineKeyboardMarkup keyboardMarkup) {
		return messageBuilder(userId, text).replyMarkup(keyboardMarkup).build();
	}

	public static EditMessageText editMessageText(long userId, int messageId, String text) {
		return editMessageBuilder(userId, messageId, text).build();
	}

	public static EditMessageText editMessageText(long userId, int messageId, String text, InlineKeyboardMarkup keyboardMarkup) {
		return editMessageBuilder(userId, messageId, text).replyMarkup(keyboardMarkup).build();
	}

	public static InlineKeyboardButton inlineKeyboardButton(String text, String callbackData) {
		return InlineKeyboardButton.builder()
				.text(text)
				.callbackData(callbackData)
				.build();
	}

	public static List<InlineKeyboardRow> inlineKeyboard(int maxButtonsInRow, List<InlineKeyboardButton> buttons) {
		ListIterator<InlineKeyboardButton> buttonIterator = buttons.listIterator();
		ArrayList<InlineKeyboardRow> inlineKeyboard = new ArrayList<>();
		for (int i = 0; i < maxButtonsInRow; i++) {
			InlineKeyboardRow keyboardRow = new InlineKeyboardRow();
			while (buttonIterator.hasNext()) {
				keyboardRow.add(buttonIterator.next());
			}
			inlineKeyboard.add(keyboardRow);
		}
		return inlineKeyboard;
	}

	private static SendMessage.SendMessageBuilder<?, ?> messageBuilder(long userId, String text) {
		return SendMessage.builder()
				.chatId(String.valueOf(userId))
				.text(text);
	}

	private static EditMessageText.EditMessageTextBuilder<?, ?> editMessageBuilder(long userId, int messageId, String text) {
		return EditMessageText.builder()
				.chatId(userId)
				.messageId(messageId)
				.text(text);
	}
}
