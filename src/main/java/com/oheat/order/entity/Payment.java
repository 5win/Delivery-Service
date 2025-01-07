package com.oheat.order.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.order.constant.PaymentState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Column(name = "payment_key", nullable = false)
    private String paymentKey;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentState state;

    @Builder
    public Payment(String paymentKey, UUID orderId, int amount, PaymentState state) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.state = state;
    }

    public boolean isConfirmed() {
        return this.state == PaymentState.CONFIRMED;
    }

    public void setConfirmed() {
        this.state = PaymentState.CONFIRMED;
    }
}
