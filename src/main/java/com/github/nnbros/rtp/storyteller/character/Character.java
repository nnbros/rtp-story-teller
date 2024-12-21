package com.github.nnbros.rtp.storyteller.character;

import java.util.List;

public record Character(String name, Gender gender,
						List<CharacterClass> classes,
						ClassDictionary activeClassDictionary) {
}
