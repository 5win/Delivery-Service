package com.oheat.order.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import jakarta.persistence.CascadeType;
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
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "orders_options_group")
public class OrderOptionGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_menu_id", nullable = false)
    private OrderMenu orderMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id", nullable = false)
    private OptionGroupJpaEntity optionGroup;

    @OneToMany(mappedBy = "orderOptionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderOption> orderOptions = new ArrayList<>();

    @Builder
    public OrderOptionGroup(OrderMenu orderMenu, OptionGroupJpaEntity optionGroup) {
        this.orderMenu = orderMenu;
        this.optionGroup = optionGroup;
    }

    public void setOrderMenu(OrderMenu orderMenu) {
        this.orderMenu = orderMenu;
    }

    public void addOrderOption(OrderOption orderOption) {
        orderOption.setOrderOptionGroup(this);
        this.orderOptions.add(orderOption);
    }

    public int calcTotalPrice() {
        return this.orderOptions.stream()
            .mapToInt(o -> o.getOption().getPrice())
            .sum();
    }
}
