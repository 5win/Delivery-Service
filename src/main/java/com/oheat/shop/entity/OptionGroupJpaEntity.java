package com.oheat.shop.entity;

import com.oheat.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "option_group")
public class OptionGroupJpaEntity extends BaseTimeEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "required")
    private boolean required;

    @Column(name = "max_num_of_select")
    private int maxNumOfSelect;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id")
    private List<OptionJpaEntity> options;

    public boolean isOptionsEmpty() {
        return options.isEmpty();
    }
}
