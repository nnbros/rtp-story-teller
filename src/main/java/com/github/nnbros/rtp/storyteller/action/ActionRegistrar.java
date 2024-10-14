package com.github.nnbros.rtp.storyteller.action;

import java.util.Map;

public interface ActionRegistrar {

	void register(Map<String, ActionPipeline> actionPipelines);
}
