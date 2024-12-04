package com.github.nnbros.rtp.storyteller.telegram.ui;

public enum DefaultElement implements Element {
	characterNotFound,
	unknownError;

	public static final String DEFAULT_GROUP_NAME = "default";

	@Override
	public String getGroupName() {
		return DEFAULT_GROUP_NAME;
	}
}
