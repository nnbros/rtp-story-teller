package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.character.SkillType;
import com.github.nnbros.rtp.storyteller.jpa.character.SkillDictionaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface SkillDictionaryRepository extends JpaRepository<SkillDictionaryEntity, Integer> {

	@Query("SELECT e FROM SkillDictionaryEntity e WHERE e.type = :type")
	List<SkillDictionaryEntity> findSkillsByType(SkillType type);

	@Query("SELECT e FROM SkillDictionaryEntity e WHERE e.type IN :types")
	List<SkillDictionaryEntity> findSkillsByTypes(Collection<SkillType> types);
}
