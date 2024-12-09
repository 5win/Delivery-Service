package com.oheat.shop.entity;

import com.oheat.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "menu")
public class MenuJpaEntity extends BaseTimeEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @OneToMany
    @JoinColumn(name = "menu_id")
    private List<OptionGroupJpaEntity> optionGroups = new ArrayList<>();

    public boolean isOptionGroupsEmpty() {
        return optionGroups.isEmpty();
    }

    public boolean isEmptyOptionGroupExists() {
        for (OptionGroupJpaEntity optionGroup : optionGroups) {
            if (optionGroup.isOptionsEmpty()) {
                return true;
            }
        }
        return false;
    }
}
