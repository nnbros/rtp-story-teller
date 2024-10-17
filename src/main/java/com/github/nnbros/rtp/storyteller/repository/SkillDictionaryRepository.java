package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.SkillDictionaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillDictionaryRepository extends JpaRepository<SkillDictionaryEntity, Integer> {
	List<SkillDictionaryEntity> findAllByClassId(int classId);
}
