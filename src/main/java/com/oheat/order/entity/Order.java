package com.oheat.order.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.order.constant.OrderState;
import com.oheat.order.constant.PayMethod;
import com.oheat.user.entity.UserJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    private UUID id;

    @Column(name = "order_state", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderState orderState;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "msg_for_shop")
    private String msgForShop;

    @Column(name = "delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "discount", nullable = false)
    private int discount;

    @Column(name = "pay_method", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PayMethod payMethod;

    @Column(name = "reviewed", nullable = false)
    private boolean reviewed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopJpaEntity shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_key")
    private Payment payment;

    @Builder
    public Order(UUID id, OrderState orderState, String address, String detailAddress, String phone, String msgForShop,
        int deliveryFee, int discount, PayMethod payMethod, boolean reviewed, ShopJpaEntity shop,
        UserJpaEntity user, Payment payment) {
        this.id = id;
        this.orderState = orderState;
        this.address = address;
        this.detailAddress = detailAddress;
        this.phone = phone;
        this.msgForShop = msgForShop;
        this.deliveryFee = deliveryFee;
        this.discount = discount;
        this.payMethod = payMethod;
        this.reviewed = reviewed;
        this.shop = shop;
        this.user = user;
        this.payment = payment;
    }

    public void addOrderMenu(OrderMenu orderMenu) {
        orderMenu.setOrder(this);
        this.orderMenus.add(orderMenu);
    }

    public int calcPayAmount() {
        int sum = this.deliveryFee - this.discount;
        for (var orderMenu : this.orderMenus) {
            sum += orderMenu.calcTotalPrice();
        }
        return sum >= 0 ? sum : 0;
    }

    public boolean validatePayAmount() {
        return this.payment != null && this.payment.getTotalAmount() == calcPayAmount();
    }
}
