package com.oheat.food.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.dto.MenuUpdateRequest;
import jakarta.persistence.CascadeType;
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
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "name"}, callSuper = false)
@Entity
@Table(name = "menu")
public class MenuJpaEntity extends BaseTimeEntity {

    @Builder
    public MenuJpaEntity(String name, int price, ShopJpaEntity shop) {
        this.name = name;
        this.price = price;
        this.shop = shop;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopJpaEntity shop;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OptionGroupJpaEntity> optionGroups = new ArrayList<>();

    public void addOptionGroup(OptionGroupJpaEntity optionGroup) {
        optionGroups.add(optionGroup);
    }

    public void updateMenu(MenuUpdateRequest updateRequest) {
        this.name = updateRequest.getName();
        this.price = updateRequest.getPrice();
    }

    public boolean isOptionGroupsEmpty() {
        return optionGroups.isEmpty();
    }
}
