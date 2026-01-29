package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.common.api.dto.character.Archetype;
import com.github.nnbros.rtp.common.api.dto.character.SkillType;

public record SkillDictionary(String name, SkillType type, Archetype effectiveAgainst) {
}
