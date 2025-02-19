package com.github.nnbros.rtp.storyteller.jpa.character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "class_dictionary", schema = "storyteller")
public class ClassDictionaryEntity {

	@Id
	@Column(name = "id", updatable = false)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true, length = 20, insertable = false, updatable = false)
	private String name;

	@Column(name = "base_hp", nullable = false, insertable = false, updatable = false)
	private Integer baseHp;

	@Column(name = "base_atk", nullable = false, insertable = false, updatable = false)
	private Integer baseAtk;

	@Column(name = "base_def", nullable = false, insertable = false, updatable = false)
	private Integer baseDef;
}
