package com.github.nnbros.rtp.storyteller.util;

import java.util.function.Consumer;

public class StorytellerUtils {

	public static <T> Consumer<T> emptyConsumer() {
		return t -> {
		};
	}
}
