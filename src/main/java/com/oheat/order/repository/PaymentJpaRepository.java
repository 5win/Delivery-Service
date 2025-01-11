package com.oheat.order.repository;

import com.oheat.order.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, String> {

}
