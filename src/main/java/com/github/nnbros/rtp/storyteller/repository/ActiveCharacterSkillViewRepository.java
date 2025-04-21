package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.ActiveCharacterSkillViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveCharacterSkillViewRepository extends JpaRepository<ActiveCharacterSkillViewEntity, Integer> {

	Optional<ActiveCharacterSkillViewEntity> findByCharacterId(int characterId);
}
