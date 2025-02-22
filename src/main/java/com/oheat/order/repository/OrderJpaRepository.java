package com.oheat.order.repository;

import com.oheat.order.entity.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, UUID>, OrderCustomRepository {

}
