package com.github.nnbros.rtp.storyteller.character;

import com.github.nnbros.rtp.common.api.dto.character.Archetype;
import com.github.nnbros.rtp.common.api.dto.character.SkillType;
import com.github.nnbros.rtp.storyteller.BotTestUtils;
import com.github.nnbros.rtp.storyteller.StorytellerTest;
import com.github.nnbros.rtp.storyteller.jpa.character.CharacterArmySkillEntity;
import com.github.nnbros.rtp.storyteller.jpa.character.CharacterClassSkillEntity;
import com.github.nnbros.rtp.storyteller.jpa.character.SkillDictionaryEntity;
import com.github.nnbros.rtp.storyteller.jpa.character.SkillDictionaryMapper;
import com.github.nnbros.rtp.storyteller.repository.CharacterArmySkillRepository;
import com.github.nnbros.rtp.storyteller.repository.CharacterClassSkillRepository;
import com.github.nnbros.rtp.storyteller.repository.SkillDictionaryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.nnbros.rtp.common.api.dto.character.SkillType.*;
import static com.github.nnbros.rtp.storyteller.BotTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SkillServiceTest extends StorytellerTest {
        @Mock
        private SkillDictionaryRepository skillDictionaryRepository;
        @Mock
        private SkillDictionaryMapper skillDictionaryMapper;
        @Mock
        private CharacterClassSkillRepository characterClassSkillRepository;
        @Mock
        private CharacterArmySkillRepository characterArmySkillRepository;

        @InjectMocks
        private SkillService skillService;

        @Test
        public void getAllSkillsByClassName() {
                SkillDictionaryEntity skillEntity = createTestSkillDictionaryEntity(5, TEST_SKILL_NAME_1, WARRIOR, Archetype.SPEARMAN);

                SkillDictionary mappedSkill = createTestSkillDictionary(TEST_SKILL_NAME_1, WARRIOR, Archetype.SPEARMAN);

                when(skillDictionaryRepository.findSkillsByType(WARRIOR)).thenReturn(List.of(skillEntity));
                when(skillDictionaryMapper.toSkillDictionary(skillEntity)).thenReturn(mappedSkill);

                Collection<SkillDictionary> result = skillService.getAllSkillsByClassName(WARRIOR.name());

                assertEquals(List.of(mappedSkill), result);
                verify(skillDictionaryRepository).findSkillsByType(WARRIOR);
                verify(skillDictionaryMapper).toSkillDictionary(skillEntity);
        }

        @Test
        public void getAllSkills() {
                SkillDictionaryEntity firstEntity = createTestSkillDictionaryEntity(7, TEST_SKILL_NAME_1, WARRIOR, Archetype.CAVALRY);

                SkillDictionaryEntity secondEntity = createTestSkillDictionaryEntity(8, TEST_SKILL_NAME_2, ROGUE, Archetype.NEUTRAL);

                SkillDictionary firstSkill = createTestSkillDictionary(TEST_SKILL_NAME_1, WARRIOR, Archetype.CAVALRY);
                SkillDictionary secondSkill = createTestSkillDictionary(TEST_SKILL_NAME_2, ROGUE, Archetype.NEUTRAL);

                when(skillDictionaryRepository.findAll()).thenReturn(List.of(firstEntity, secondEntity));
                when(skillDictionaryMapper.toSkillDictionary(firstEntity)).thenReturn(firstSkill);
                when(skillDictionaryMapper.toSkillDictionary(secondEntity)).thenReturn(secondSkill);

                Collection<SkillDictionary> result = skillService.getAllSkills();

                assertEquals(List.of(firstSkill, secondSkill), result);
                verify(skillDictionaryRepository).findAll();
                verify(skillDictionaryMapper).toSkillDictionary(firstEntity);
                verify(skillDictionaryMapper).toSkillDictionary(secondEntity);
        }

        @Test
        @SuppressWarnings("unchecked")
        public void saveDefaultCharacterClassSkillsPersistsClassSkills() {
                ClassDictionary classDictionary = BotTestUtils.createTestClassDictionary();
                SkillDictionaryEntity basicCharacterSkill = createTestSkillDictionaryEntity(11, null, BASIC_CHARACTER, null);
                SkillDictionaryEntity classSpecificSkill = createTestSkillDictionaryEntity(12, null, WARRIOR, null);

                when(skillDictionaryRepository.findSkillsByTypes(anyCollection()))
                                .thenReturn(List.of(basicCharacterSkill, classSpecificSkill));

                skillService.saveDefaultCharacterClassSkills(classDictionary, 42);

                ArgumentCaptor<Collection<SkillType>> skillTypesCaptor = ArgumentCaptor.forClass(Collection.class);
                verify(skillDictionaryRepository).findSkillsByTypes(skillTypesCaptor.capture());
                assertEquals(EnumSet.of(BASIC_CHARACTER, WARRIOR), EnumSet.copyOf(skillTypesCaptor.getValue()));

                ArgumentCaptor<List<CharacterClassSkillEntity>> savedClassSkillsCaptor = ArgumentCaptor.forClass(List.class);
                verify(characterClassSkillRepository).saveAllAndFlush(savedClassSkillsCaptor.capture());
                List<CharacterClassSkillEntity> savedSkills = savedClassSkillsCaptor.getValue();

                assertEquals(2, savedSkills.size());
                assertTrue(savedSkills.stream()
                                .allMatch(entity -> entity.getCharacterClassId().equals(42)));
                Set<Integer> savedSkillIds = savedSkills.stream()
                                .map(CharacterClassSkillEntity::getSkillId)
                                .collect(Collectors.toSet());
                assertEquals(Set.of(11, 12), savedSkillIds);
        }

        @Test
        @SuppressWarnings("unchecked")
        public void saveDefaultCharacterArmySkillsPersistsArmySkills() {
                ArmyDictionary armyDictionary = createTestArmyDictionary();
                SkillDictionaryEntity basicArmySkill = createTestSkillDictionaryEntity(21, null, BASIC_ARMY, null);
                SkillDictionaryEntity armySpecificSkill = createTestSkillDictionaryEntity(22, null, SkillType.SCOUTS, null);
                SkillDictionaryEntity archetypeSkill = createTestSkillDictionaryEntity(23, null, SkillType.SWORDSMAN, null);

                when(skillDictionaryRepository.findSkillsByTypes(anyCollection()))
                                .thenReturn(List.of(basicArmySkill, armySpecificSkill, archetypeSkill));

                skillService.saveDefaultCharacterArmySkills(armyDictionary, 77);

                ArgumentCaptor<Collection<SkillType>> skillTypesCaptor = ArgumentCaptor.forClass(Collection.class);
                verify(skillDictionaryRepository).findSkillsByTypes(skillTypesCaptor.capture());
                assertEquals(EnumSet.of(BASIC_ARMY, SkillType.SEEKERS, SkillType.SWORDSMAN),
                                EnumSet.copyOf(skillTypesCaptor.getValue()));

                ArgumentCaptor<List<CharacterArmySkillEntity>> savedArmySkillsCaptor = ArgumentCaptor.forClass(List.class);
                verify(characterArmySkillRepository).saveAllAndFlush(savedArmySkillsCaptor.capture());
                List<CharacterArmySkillEntity> savedSkills = savedArmySkillsCaptor.getValue();

                assertEquals(3, savedSkills.size());
                assertTrue(savedSkills.stream()
                                .allMatch(entity -> entity.getCharacterArmyId().equals(77)));
                Set<Integer> savedSkillIds = savedSkills.stream()
                                .map(CharacterArmySkillEntity::getSkillId)
                                .collect(Collectors.toSet());
                assertEquals(Set.of(21, 22, 23), savedSkillIds);
        }
}
