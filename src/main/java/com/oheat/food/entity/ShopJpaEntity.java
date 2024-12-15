package com.oheat.food.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.dto.ShopUpdateRequest;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = {"phone", "category", "minimumOrderAmount", "menuSet", "menuGroups"},
    callSuper = false)
@Entity
@Table(name = "shop")
public class ShopJpaEntity extends BaseTimeEntity {

    @Builder
    public ShopJpaEntity(String name, String phone, CategoryJpaEntity category,
        int minimumOrderAmount) {
        this.name = name;
        this.phone = phone;
        this.category = category;
        this.minimumOrderAmount = minimumOrderAmount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "name", nullable = false)
    private CategoryJpaEntity category;

    @Column(name = "minimum_order_amount", nullable = false)
    private int minimumOrderAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shop")
    private final Set<MenuJpaEntity> menuSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shop")
    private final List<MenuGroupJpaEntity> menuGroups = new ArrayList<>();

    public void updateShopInfo(ShopUpdateRequest updateRequest, CategoryJpaEntity category) {
        this.name = updateRequest.getName();
        this.phone = updateRequest.getPhone();
        this.category = category;
        this.minimumOrderAmount = updateRequest.getMinimumOrderAmount();
    }

    public void addMenu(MenuJpaEntity menu) {
        if (isDuplicateMenuName(menu)) {
            throw new DuplicateMenuException();
        }
        menuSet.add(menu);
    }

    private boolean isDuplicateMenuName(MenuJpaEntity menu) {
        Optional<MenuJpaEntity> target = menuSet.stream()
            .filter(menuJpaEntity -> menuJpaEntity.getName().equals(menu.getName()))
            .findFirst();
        return target.isPresent();
    }
}
