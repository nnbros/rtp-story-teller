package com.github.nnbros.rtp.storyteller.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents localized values dictionary. Includes such dictionaries as Classes, Skills, etc.
 * Can be used without concerning some NPE, because there is a {@code LocalizationValidator}
 * which validates the consistency between DB and localization files.
 *
 * @see com.github.nnbros.rtp.storyteller.validation.LocalizationValidator
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "localization")
public class Localization {
	@NotNull
	private Map<String, Clazz> classes = new HashMap<>();
	@NotNull
	private Map<String, Army> armies = new HashMap<>();
	@NotNull
	private Map<String, Skill> skills = new HashMap<>();

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Clazz {
		@NotBlank
		private String name;
		@NotBlank
		private String description;
	}

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Army {
		@NotBlank
		private String name;
		@NotBlank
		private String description;
		@NotBlank
		private String type;
	}

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Skill {
		@NotBlank
		private String name;
		private String description;
	}
}
