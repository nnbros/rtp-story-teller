package com.github.nnbros.rtp.storyteller.telegram.ui;

public record Parameter(ParameterKey keyHolder, Object value) {

	public static Parameter of(ParameterKey keyHolder, Object value) {
		return new Parameter(keyHolder, value);
	}
}
