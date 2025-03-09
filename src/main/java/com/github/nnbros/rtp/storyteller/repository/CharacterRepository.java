package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Integer> {

	@Query("select ce from CharacterEntity ce join fetch ce.activeClassDictionary where ce.userId=:userId")
	Optional<CharacterEntity> findDictionariesByUserId(long userId);

	Optional<CharacterEntity> findByUserId(long userId);
}
