package com.github.nnbros.rtp.storyteller.telegram.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO move to common lib?
public class ParametersBuilder {

	public Map<String, String> buildParameters(Parameter... parameters) {
		return buildParameters((Arrays.stream(parameters)));
	}

	public Map<String, String> buildParameters(List<Parameter> parameters) {
		return buildParameters(parameters.stream());
	}

	private Map<String, String> buildParameters(Stream<Parameter> parameters) {
		return parameters.collect(Collectors.toMap(
				parameter -> parameter.keyHolder().getKey(),
				parameter -> parameter.value().toString()));
	}
}
