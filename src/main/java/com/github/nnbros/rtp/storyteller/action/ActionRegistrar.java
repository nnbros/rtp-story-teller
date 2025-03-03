package com.github.nnbros.rtp.storyteller.action;

import java.util.Map;

public interface ActionRegistrar {

	Map<String, ActionPipeline> getActionPipelines();

	default void register(Map<String, ActionPipeline> actionPipelines) {
		actionPipelines.putAll(getActionPipelines());
	}
}
