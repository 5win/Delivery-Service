package com.oheat.food.repository;

import com.oheat.food.entity.MenuJpaEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuJpaRepository menuJpaRepository;

    @Override
    public void save(MenuJpaEntity menuJpaEntity) {
        menuJpaRepository.save(menuJpaEntity);
    }

    @Override
    public Optional<MenuJpaEntity> findById(Long menuId) {
        return menuJpaRepository.findById(menuId);
    }

    @Override
    public Optional<MenuJpaEntity> findByName(String name) {
        return menuJpaRepository.findByName(name);
    }

    @Override
    public void deleteById(Long menuId) {
        menuJpaRepository.deleteById(menuId);
    }
}
