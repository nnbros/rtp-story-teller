package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Integer> {

	Optional<CharacterEntity> findByUserId(long userId);

	@Query("""
			select ce from CharacterEntity ce \
			join fetch ce.activeClass ac \
			join fetch ce.activeArmy aa \
			where ce.userId=:userId""")
	Optional<CharacterEntity> findWithActiveClassAndArmyByUserId(long userId);

	@Query("""
			select ce from CharacterEntity ce \
			join fetch ce.activeClass ac \
			join fetch ce.activeArmy aa \
			join fetch ce.activeSkills as \
			where ce.userId=:userId""")
	Optional<CharacterEntity> findWithActiveClassArmyAndSkillsByUserId(long userId);
}
