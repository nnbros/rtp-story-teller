package com.github.nnbros.rtp.storyteller.telegram;

import com.github.nnbros.rtp.storyteller.exception.IllegalUpdateException;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class BotUtils {

	public static Long getUserId(Update update, UpdateType updateType) throws IllegalUpdateException {
		return switch (updateType) {
			case MESSAGE, COMMAND -> getUserId(update.getMessage());
			case EDITED_MESSAGE, EDITED_COMMAND -> getUserId(update.getEditedMessage());
			case CALLBACK_QUERY -> getUserId(update.getCallbackQuery());
			case UNKNOWN ->
					throw new IllegalUpdateException("Unable to get the user id from update %s", update.getUpdateId());
		};
	}

	private static Long getUserId(Message message) {
		return message.getFrom()
				.getId();
	}

	private static Long getUserId(CallbackQuery callbackQuery) {
		return callbackQuery.getFrom()
				.getId();
	}
}
