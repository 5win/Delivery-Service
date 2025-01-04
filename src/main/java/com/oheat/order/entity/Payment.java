package com.oheat.order.entity;

import com.oheat.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"paymentKey", "orderId", "amount"}, callSuper = false)
@Entity
@Table(name = "payment")
public class Payment extends BaseTimeEntity {

    @Id
    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "amount")
    private int amount;

    @Builder
    public Payment(String paymentKey, UUID orderId, int amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }

    public boolean validateAmount(int amount) {
        return this.amount == amount;
    }
}
