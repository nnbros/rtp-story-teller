package com.github.nnbros.rtp.storyteller.action.registration;

import com.github.nnbros.rtp.common.api.dto.character.Gender;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@RequiredArgsConstructor
public class CharacterRequest {
	@NonNull
	private final Long userId;
	private Gender gender;
	private String name;
	private String className;
}
