package com.oheat.shop.entity;

import com.oheat.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private Set<MenuJpaEntity> menus = new HashSet<>();

    public void addMenu(MenuJpaEntity menu) {
        this.menus.add(menu);
    }

    public boolean containsMenu(MenuJpaEntity menu) {
        return menus.contains(menu);
    }
}
