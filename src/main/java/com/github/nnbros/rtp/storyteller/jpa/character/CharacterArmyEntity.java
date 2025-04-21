package com.github.nnbros.rtp.storyteller.jpa.character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "character_army", schema = "storyteller")
public class CharacterArmyEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "character_id", referencedColumnName = "id", nullable = false)
	private CharacterEntity character;

	@Column(name = "army_id", nullable = false)
	private Integer armyDictionaryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "army_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private ArmyDictionaryEntity armyDictionary;

	@Column(name = "level", nullable = false, insertable = false)
	private Integer level;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updatedAt;
}
