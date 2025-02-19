package com.github.nnbros.rtp.storyteller.jpa.character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "skill_dictionary", schema = "storyteller")
public class SkillDictionaryEntity {

	@Id
	@Column(name = "id", updatable = false)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true, length = 32, insertable = false, updatable = false)
	private String name;

	@Column(name = "class_id", nullable = false, insertable = false, updatable = false)
	private Integer classId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private ClassDictionaryEntity classDictionary;
}
