package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.ClassDictionaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassDictionaryRepository extends JpaRepository<ClassDictionaryEntity, Integer> {

	@Query("SELECT e.id FROM ClassDictionaryEntity e WHERE e.name = :name")
	Optional<Integer> findIdByName(String name);

	@Query("SELECT e FROM ClassDictionaryEntity e JOIN FETCH e.skills WHERE e.name = :name")
	Optional<ClassDictionaryEntity> findSkillsByName(String name);
}
