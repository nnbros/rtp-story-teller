package com.github.nnbros.rtp.storyteller.character;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CharacterRequest {
	private final Long userId;
	private Gender gender;
	private String name;
	private String className;
	private Integer lastMessageId;
}
