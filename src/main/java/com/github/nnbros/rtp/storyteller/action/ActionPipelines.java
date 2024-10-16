package com.github.nnbros.rtp.storyteller.action;

import java.util.function.Consumer;
import java.util.function.Function;

public class ActionPipelines {

	public static <T> ActionPipeline create(Function<ActionContext, T> actionProcessor, Consumer<T> nextActionHandler) {
		return (actionContext) -> {
			T result = actionProcessor.apply(actionContext);
			nextActionHandler.accept(result);
		};
	}

	public static <T> ActionPipeline create(Function<ActionContext, ActionResult<T>> actionProcessor,
											Consumer<T> nextActionHandler,
											Consumer<T> altNextActionHandler) {
		return (actionContext) -> {
			ActionResult<T> result = actionProcessor.apply(actionContext);
			if (result.isSuccessful()) {
				nextActionHandler.accept(result.value());
			} else {
				altNextActionHandler.accept(result.value());
			}
		};
	}

	public static ActionPipeline create(Consumer<ActionContext> actionProcessor, Consumer<ActionContext> nextActionHandler) {
		return (actionContext) -> {
			actionProcessor.accept(actionContext);
			nextActionHandler.accept(actionContext);
		};
	}
}
