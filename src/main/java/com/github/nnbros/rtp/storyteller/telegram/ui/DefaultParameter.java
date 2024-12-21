package com.github.nnbros.rtp.storyteller.telegram.ui;

import com.github.guronas.telegram.bot.elements.parameter.ParameterKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultParameter implements ParameterKey {
	CHAT_ID("chatId"),
	MESSAGE_ID("messageId"),
	CALLBACK_QUERY_ID("callbackQueryId");

	private final String key;
}
