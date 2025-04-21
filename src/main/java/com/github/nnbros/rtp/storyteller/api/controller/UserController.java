package com.github.nnbros.rtp.storyteller.api.controller;

import com.github.nnbros.rtp.storyteller.character.CharacterService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${storyteller.api.base-url}${storyteller.api.user-endpoint-prefix}")
public class UserController {
	private final CharacterService characterService;

	@GetMapping("/{userId}/character")
	public ResponseEntity<?> getCharacterDetailedView(@NotNull @PathVariable long userId) {
		try {
			return ResponseEntity.ok(characterService.getDetailedCharacterWithSkillsViewByUserId(userId));
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(e.getMessage());
		}
	}

}
