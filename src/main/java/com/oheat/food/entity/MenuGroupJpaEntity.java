package com.oheat.food.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.dto.MenuGroupUpdateRequest;
import com.oheat.food.exception.DuplicateMenuException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "name"}, callSuper = false)
@Entity
@Table(name = "menu_group")
public class MenuGroupJpaEntity extends BaseTimeEntity {

    @Builder
    public MenuGroupJpaEntity(String name, ShopJpaEntity shop) {
        this.name = name;
        this.shop = shop;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopJpaEntity shop;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menuGroup")
    private final Set<MenuGroupMappingJpaEntity> menuGroupMappingSet = new HashSet<>();

    public void addMenuMapping(MenuGroupMappingJpaEntity menuGroupMapping) {
        if (containsMenu(menuGroupMapping)) {
            throw new DuplicateMenuException();
        }
        this.menuGroupMappingSet.add(menuGroupMapping);
    }

    private boolean containsMenu(MenuGroupMappingJpaEntity menuGroupMapping) {
        return menuGroupMappingSet.contains(menuGroupMapping);
    }

    public void updateMenuGroup(MenuGroupUpdateRequest updateRequest) {
        this.name = updateRequest.getName();
    }
}
