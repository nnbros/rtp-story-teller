package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.Archetype;
import com.github.nnbros.rtp.storyteller.character.SkillType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.Immutable;

import java.io.Serializable;

@Getter
@Entity
@Immutable
@Table(name = "active_character_skill_view", schema = "storyteller")
public class ActiveCharacterSkillViewEntity implements Serializable {

	@NaturalId
	@Column(name = "character_id", nullable = false)
	private Integer characterId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
	private CharacterEntity character;

	@Id
	@Column(name = "character_skill_id", nullable = false)
	private Integer characterSkillId;

	@Column(name = "skill_id", nullable = false)
	private Integer skillDictionaryId;

	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private SkillType skillType;

	@Enumerated(EnumType.STRING)
	@Column(name = "effective_against")
	private Archetype effectiveAgainst;
}
