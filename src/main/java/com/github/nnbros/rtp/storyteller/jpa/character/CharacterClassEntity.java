package com.github.nnbros.rtp.storyteller.jpa.character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "character_class", schema = "storyteller")
public class CharacterClassEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "character_id", referencedColumnName = "id", nullable = false)
	private CharacterEntity character;

	@Column(name = "class_id", nullable = false)
	private Integer classDictionaryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private ClassDictionaryEntity classDictionary;

	@Column(name = "experience", nullable = false, insertable = false)
	private Long experience;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime updatedAt;
}
