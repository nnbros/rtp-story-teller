package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.ActiveCharacterArmyViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveCharacterArmyViewRepository extends JpaRepository<ActiveCharacterArmyViewEntity, Integer> {

	Optional<ActiveCharacterArmyViewEntity> findByCharacterId(int characterId);
}
