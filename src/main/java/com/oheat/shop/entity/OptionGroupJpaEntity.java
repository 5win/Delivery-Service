package com.oheat.shop.entity;

import com.oheat.common.BaseTimeEntity;
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
@EqualsAndHashCode(exclude = {"required", "maxNumOfSelect", "options"}, callSuper = false)
@Entity
@Table(name = "option_group")
public class OptionGroupJpaEntity extends BaseTimeEntity {

    @Builder
    public OptionGroupJpaEntity(String name, MenuJpaEntity menu, boolean required,
        int maxNumOfSelect) {
        this.name = name;
        this.menu = menu;
        this.required = required;
        this.maxNumOfSelect = maxNumOfSelect;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "required")
    private boolean required;

    @Column(name = "max_num_of_select")
    private int maxNumOfSelect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuJpaEntity menu;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "optionGroup")
    private final List<OptionJpaEntity> options = new ArrayList<>();

    public void addOption(OptionJpaEntity option) {
        options.add(option);
    }

    public boolean isOptionsEmpty() {
        return options.isEmpty();
    }
}
