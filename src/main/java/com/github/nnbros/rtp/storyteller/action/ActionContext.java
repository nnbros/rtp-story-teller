package com.github.nnbros.rtp.storyteller.action;

import com.github.nnbros.rtp.storyteller.telegram.UpdateType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.meta.api.objects.Update;

public record ActionContext(
		@NonNull String action,
		@NonNull Long userId,
		@NonNull UpdateType updateType,
		@NonNull Update update,
		@Nullable Integer messageId,
		@Nullable String data
) {
}
