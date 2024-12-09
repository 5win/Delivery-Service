package com.oheat.shop.jpaTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.repository.CategoryJpaRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("이름이 중복되지 않으면, 카테고리 저장 성공")
    void categoryNameNotDuplicate_thenSuccess() {
        categoryJpaRepository.save(
            CategoryJpaEntity.builder()
                .name("치킨")
                .build()
        );
    }

    @Test
    @DisplayName("이름이 중복이면, 카테고리 저장 실패")
    void categoryNameDuplicate_thenFail() {
        categoryJpaRepository.save(
            CategoryJpaEntity.builder()
                .name("치킨")
                .build()
        );

        assertThrows(DataIntegrityViolationException.class, () -> {
            categoryJpaRepository.save(
                CategoryJpaEntity.builder()
                    .name("치킨")
                    .build()
            );
        });
    }

    @Test
    @DisplayName("JPA로 카테고리 3개를 저장하고 카테고리 목록을 조회하면, 목록의 size가 3이어야 함")
    void usingJpa_givenThreeCategories_whenFindAll_thenListSizeThree() {
        categoryJpaRepository.save(CategoryJpaEntity.builder().name("치킨").build());
        categoryJpaRepository.save(CategoryJpaEntity.builder().name("피자").build());
        categoryJpaRepository.save(CategoryJpaEntity.builder().name("야식").build());

        List<CategoryJpaEntity> result = categoryJpaRepository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Disabled
    @Test
    @DisplayName("JPA 영속성 컨테스트에 category 엔티티 객체 정보가 없더라도, equals 비교 연산이 성공해야 함")
    void givenPersistenceContextIsEmpty_whenCategoryEqualsOperation_thenSuccess() {
        CategoryJpaEntity jpaEntity = CategoryJpaEntity.builder().name("치킨").build();

        entityManager.persist(jpaEntity);
        entityManager.flush();
        entityManager.clear();

        CategoryJpaEntity result = entityManager.find(CategoryJpaEntity.class, 1L);

        Assertions.assertThat(result).isEqualTo(jpaEntity);
    }
}
