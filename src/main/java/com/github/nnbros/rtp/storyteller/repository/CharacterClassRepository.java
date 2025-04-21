package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.CharacterClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterClassRepository extends JpaRepository<CharacterClassEntity, Integer> {

	@Query("select cce from CharacterClassEntity cce join cce.character c join cce.classDictionary cd " +
			"where c.userId=:userId and cd.name=:className")
	Optional<CharacterClassEntity> findByClassIdAndCharacterId(long userId, String className);
}
