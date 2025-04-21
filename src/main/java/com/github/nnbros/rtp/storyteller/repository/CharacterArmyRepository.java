package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.CharacterArmyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterArmyRepository extends JpaRepository<CharacterArmyEntity, Integer> {

	@Query("select cae from CharacterArmyEntity cae join cae.character c join cae.armyDictionary ad " +
			"where c.userId=:userId and ad.name=:armyName")
	Optional<CharacterArmyEntity> findByClassIdAndCharacterId(long userId, String armyName);
}
