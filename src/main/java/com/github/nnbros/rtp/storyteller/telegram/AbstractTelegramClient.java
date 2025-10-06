package com.github.nnbros.rtp.storyteller.telegram;

import com.github.guronas.telegram.bot.elements.TelegramElementRegistry;
import com.github.guronas.telegram.bot.elements.exception.ElementNotFoundException;
import com.github.guronas.telegram.bot.elements.parameter.DynamicParameters;
import com.github.guronas.telegram.bot.elements.parameter.Parameter;
import com.github.nnbros.rtp.storyteller.exception.StoryTellerRuntimeException;
import com.github.nnbros.rtp.storyteller.telegram.ui.Element;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractTelegramClient {
	public static final String CONFIRMED_OPTION_TEMPLATE = "%s%s";
	public static final String CALLBACK_DATA_TEMPLATE = "%s:%s";

	private final TelegramClient telegramClient;
	protected final TelegramElementRegistry elementRegistry;

	protected <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method request) {
		try {
			telegramClient.execute(request);
		} catch (TelegramApiException e) {
			//TODO retry
			throw new StoryTellerRuntimeException(e);
		}
	}

	protected <T> T buildElement(Element element, Class<T> elementType) {
		return buildElement(element.getGroupName(), element.name(), Collections.emptyMap(), elementType);
	}

	protected String buildText(Element element) {
		return buildElement(element.getGroupName(), element.name(), Collections.emptyMap(), String.class);
	}

	protected BotApiMethod<?> buildBotApiMethod(Element element) {
		return buildBotApiMethod(element.getGroupName(), element.name(), Collections.emptyMap());
	}

	protected BotApiObject buildBotApiObject(Element element) {
		return buildBotApiObject(element.getGroupName(), element.name(), Collections.emptyMap());
	}

	protected <T> T buildElement(Element element, Map<String, String> params, Class<T> elementType) {
		return buildElement(element.getGroupName(), element.name(), params, elementType);
	}

	protected <T> T buildElement(Element element, List<Parameter> params, Class<T> elementType) {
		return buildElement(element.getGroupName(), element.name(), params, elementType);
	}

	protected String buildText(Element element, Map<String, String> params) {
		return buildElement(element.getGroupName(), element.name(), params, String.class);
	}

	protected BotApiMethod<?> buildBotApiMethod(Element element, Map<String, String> params) {
		return buildBotApiMethod(element.getGroupName(), element.name(), params);
	}

	protected BotApiMethod<?> buildBotApiMethod(Element element, Map<String, String> params, Map<String, DynamicParameters> dynamicParams) {
		return buildBotApiMethod(element.getGroupName(), element.name(), params, dynamicParams);
	}

	protected BotApiObject buildBotApiObject(Element element, Map<String, String> params) {
		return buildBotApiObject(element.getGroupName(), element.name(), params);
	}

	protected BotApiMethod<?> buildBotApiMethod(String groupName, String elementName, Map<String, String> params) {
		try {
			return elementRegistry.buildBotApiMethod(groupName, elementName, params);
		} catch (ElementNotFoundException e) {
			throw new StoryTellerRuntimeException(e);
		}
	}

	protected BotApiMethod<?> buildBotApiMethod(String groupName,
												String elementName,
												Map<String, String> params,
												Map<String, DynamicParameters> dynamicParams) {
		try {
			return elementRegistry.buildBotApiMethod(groupName, elementName, params, dynamicParams);
		} catch (ElementNotFoundException e) {
			throw new StoryTellerRuntimeException(e);
		}
	}

	protected BotApiObject buildBotApiObject(String groupName, String elementName, Map<String, String> params) {
		try {
			return elementRegistry.buildBotApiObject(groupName, elementName, params);
		} catch (ElementNotFoundException e) {
			throw new StoryTellerRuntimeException(e);
		}
	}

	protected <T> T buildElement(String groupName, String elementName, Map<String, String> params, Class<T> elementType) {
		try {
			return elementRegistry.buildElement(groupName, elementName, params, elementType);
		} catch (ElementNotFoundException e) {
			throw new StoryTellerRuntimeException(e);
		}
	}

	protected <T> T buildElement(String groupName, String elementName, List<Parameter> params, Class<T> elementType) {
		try {
			return elementRegistry.buildElement(groupName, elementName, params, elementType);
		} catch (ElementNotFoundException e) {
			throw new StoryTellerRuntimeException(e);
		}
	}

	protected String buildText(String groupName, String elementName, Map<String, String> params) {
		try {
			return elementRegistry.buildElement(groupName, elementName, params, String.class);
		} catch (ElementNotFoundException e) {
			throw new StoryTellerRuntimeException(e);
		}
	}
}
