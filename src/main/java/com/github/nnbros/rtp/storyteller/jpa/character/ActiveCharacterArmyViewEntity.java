package com.github.nnbros.rtp.storyteller.jpa.character;

import com.github.nnbros.rtp.common.api.dto.character.Archetype;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.Immutable;

import java.io.Serializable;

@Getter
@Entity
@Immutable
@Table(name = "active_character_army_view", schema = "storyteller")
public class ActiveCharacterArmyViewEntity implements Serializable {

    @Id
    @Column(name = "character_id", nullable = false)
    private Integer characterId;

    @Column(name = "character_army_id", nullable = false)
    private Integer characterArmyId;

    @Column(name = "army_id", nullable = false)
    private Integer armyDictionaryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Archetype type;

    @Column(name = "base_quantity", nullable = false)
    private Integer baseQuantity;

    @Column(name = "base_hp", nullable = false)
    private Integer baseHp;

    @Column(name = "base_atk", nullable = false)
    private Integer baseAtk;

    @Column(name = "base_def", nullable = false)
    private Integer baseDef;

    @Column(name = "advantage_bonus", nullable = false)
    private Float advantageBonus;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "tier", nullable = false)
    private Integer tier;
}
