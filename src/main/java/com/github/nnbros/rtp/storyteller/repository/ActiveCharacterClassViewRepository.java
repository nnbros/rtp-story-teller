package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.ActiveCharacterClassViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveCharacterClassViewRepository extends JpaRepository<ActiveCharacterClassViewEntity, Integer> {

	Optional<ActiveCharacterClassViewEntity> findByCharacterId(int characterId);
}
