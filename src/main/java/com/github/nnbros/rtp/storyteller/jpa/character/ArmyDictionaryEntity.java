package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.ArmyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "army_dictionary", schema = "storyteller")
public class ArmyDictionaryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", insertable = false, updatable = false)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true, length = 20, insertable = false, updatable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, insertable = false, updatable = false)
	private ArmyType type;

	@Column(name = "base_hp", nullable = false, insertable = false, updatable = false)
	private Integer baseHp;

	@Column(name = "base_hp", nullable = false, insertable = false, updatable = false)
	private Integer baseCount;

	@Column(name = "base_atk", nullable = false, insertable = false, updatable = false)
	private Integer baseAtk;

	@Column(name = "base_def", nullable = false, insertable = false, updatable = false)
	private Integer baseDef;

	@Column(name = "tier", nullable = false, insertable = false, updatable = false)
	private Integer tier;
}
