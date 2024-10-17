package com.github.nnbros.rtp.storyteller.repository;

import com.github.nnbros.rtp.storyteller.jpa.character.CharacterClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterClassRepository extends JpaRepository<CharacterClassEntity, Integer> {
}
