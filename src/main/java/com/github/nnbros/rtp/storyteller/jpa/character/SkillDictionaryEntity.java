package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.common.api.dto.character.Archetype;
import com.github.nnbros.rtp.common.api.dto.character.SkillType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "skill_dictionary", schema = "storyteller")
public class SkillDictionaryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", insertable = false, updatable = false)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true, length = 32, insertable = false, updatable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, insertable = false, updatable = false)
	private SkillType type;

	@Enumerated(EnumType.STRING)
	@Column(name = "effective_against", insertable = false, updatable = false)
	private Archetype effectiveAgainst;
}
