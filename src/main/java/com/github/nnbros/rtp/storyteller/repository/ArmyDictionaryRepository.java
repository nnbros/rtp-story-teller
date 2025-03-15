package com.github.nnbros.rtp.storyteller.repository;

import java.util.Optional;

import com.github.nnbros.rtp.storyteller.jpa.character.ArmyDictionaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArmyDictionaryRepository extends JpaRepository<ArmyDictionaryEntity, Integer> {

	Optional<ArmyDictionaryEntity> findByName(String name);

	@Query("SELECT e.id FROM ArmyDictionaryEntity e WHERE e.name = :name")
	Optional<Integer> findIdByName(String name);
}
