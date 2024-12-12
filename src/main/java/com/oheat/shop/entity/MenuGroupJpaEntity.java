package com.oheat.shop.entity;

import com.oheat.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "menu_group")
public class MenuGroupJpaEntity extends BaseTimeEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menuGroup")
    private Set<MenuGroupMappingJpaEntity> menuGroupMappingSet = new HashSet<>();

    public void addMenuMapping(MenuGroupMappingJpaEntity menuGroupMapping) {
        this.menuGroupMappingSet.add(menuGroupMapping);
    }

    public boolean containsMenu(MenuGroupMappingJpaEntity menuGroupMapping) {
        return menuGroupMappingSet.contains(menuGroupMapping);
    }
}
