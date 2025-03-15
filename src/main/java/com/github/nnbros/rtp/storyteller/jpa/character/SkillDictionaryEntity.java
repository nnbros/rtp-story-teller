package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.SkillType;
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

	@Column(name = "effective_against", insertable = false, updatable = false)
	private Integer effectiveAgainst;

	@Column(name = "hero_atk", nullable = false, insertable = false, updatable = false)
	private Integer heroAtk;

	@Column(name = "hero_def", nullable = false, insertable = false, updatable = false)
	private Integer heroDef;

	@Column(name = "army_atk", nullable = false, insertable = false, updatable = false)
	private Integer armyAtk;

	@Column(name = "army_def", nullable = false, insertable = false, updatable = false)
	private Integer armyDef;

	@Column(name = "hero_split", nullable = false, insertable = false, updatable = false)
	private Integer heroSplit;

	@Column(name = "army_split", nullable = false, insertable = false, updatable = false)
	private Integer armySplit;
}
