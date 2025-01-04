package com.oheat.order;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrderIntegrationTest {

    @Disabled
    @Test
    @DisplayName("장바구니를 비우는 과정에서 문제 발생 시, 저장된 주문 데이터를 롤백한다.")
    void test1() {

    }

    @Disabled
    @Test
    @DisplayName("주문 데이터를 저장하는 과정(save)에서 문제 발생 시, 결제를 취소한다.")
    void test2() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니를 비우는 과정에서 문제 발생 시, 결제를 취소한다.")
    void test3() {

    }

    @Disabled
    @Test
    @DisplayName("결제부터 주문 성공까지의 프로세스를 성공한다.")
    void test4() {

    }
}
