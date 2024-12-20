package com.oheat.order.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.user.entity.UserJpaEntity;
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
@Table(name = "cart")
public class CartJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopJpaEntity shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuJpaEntity menu;

    @OneToMany(mappedBy = "cart")
    private List<CartOptionGroup> cartOptionGroup = new ArrayList<>();

    @Builder
    public CartJpaEntity(int amount, UserJpaEntity user, ShopJpaEntity shop, MenuJpaEntity menu) {
        this.amount = amount;
        this.user = user;
        this.shop = shop;
        this.menu = menu;
    }

    public void changeUser(UserJpaEntity user) {
        this.user = user;
    }
}
