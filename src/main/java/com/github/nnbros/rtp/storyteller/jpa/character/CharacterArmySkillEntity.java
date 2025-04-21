package com.github.nnbros.rtp.storyteller.jpa.character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "character_army_skill", schema = "storyteller")
public class CharacterArmySkillEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "character_army_id", nullable = false)
	private Integer characterArmyId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "character_army_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private CharacterArmyEntity characterArmyCharacterClass;

	@Column(name = "skill_id", nullable = false)
	private Integer skillId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "skill_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private SkillDictionaryEntity skill;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updatedAt;
}
