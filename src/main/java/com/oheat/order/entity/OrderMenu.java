package com.oheat.order.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.entity.MenuJpaEntity;
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
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "orders_menu")
public class OrderMenu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuJpaEntity menu;

    @OneToMany(mappedBy = "orderMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderOptionGroup> orderOptionGroups = new ArrayList<>();

    @Builder
    public OrderMenu(int amount, Order order, MenuJpaEntity menu) {
        this.amount = amount;
        this.order = order;
        this.menu = menu;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void addOrderOptionGroup(OrderOptionGroup orderOptionGroup) {
        orderOptionGroup.setOrderMenu(this);
        this.orderOptionGroups.add(orderOptionGroup);
    }

    public int calcTotalPrice() {
        int sum = this.menu.getPrice();
        for (var orderOptionGroup : this.orderOptionGroups) {
            sum += orderOptionGroup.calcTotalPrice();
        }
        return sum * this.amount;
    }
}
