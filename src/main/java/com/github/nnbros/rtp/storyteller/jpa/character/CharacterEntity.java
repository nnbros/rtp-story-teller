package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.Gender;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "character", schema = "storyteller")
public class CharacterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false)
	private Gender gender;

	@Column(name = "name", nullable = false, unique = true, length = 32)
	private String name;

	@Column(name = "created_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "active_class_id", nullable = false)
	private Integer activeClassDictionaryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "active_class_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private ClassDictionaryEntity activeClassDictionary;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinTable(
			name = "character_class",
			joinColumns = @JoinColumn(name = "character_id"),
			inverseJoinColumns = @JoinColumn(name = "class_id")
	)
	private CharacterClassEntity characterClass;

	@OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CharacterClassEntity> classes;
}

