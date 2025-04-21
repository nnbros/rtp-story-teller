package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.storyteller.character.Archetype;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.Immutable;

import java.io.Serializable;

@Getter
@Entity
@Immutable
@Table(name = "active_character_class_view", schema = "storyteller")
public class ActiveCharacterClassViewEntity implements Serializable {

    @Id
    @Column(name = "character_id", nullable = false)
    private Integer characterId;

    @Column(name = "character_class_id", nullable = false)
    private Integer characterClassId;

    @Column(name = "class_id", nullable = false)
    private Integer classDictionaryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Archetype type;

    @Column(name = "base_hp", nullable = false)
    private Integer baseHp;

    @Column(name = "base_atk", nullable = false)
    private Integer baseAtk;

    @Column(name = "base_def", nullable = false)
    private Integer baseDef;

    @Column(name = "advantage_bonus", nullable = false)
    private Float advantageBonus;

    @Column(name = "experience", nullable = false)
    private Long experience;
}
