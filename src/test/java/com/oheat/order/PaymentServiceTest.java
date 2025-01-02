package com.oheat.order;

import com.oheat.order.entity.Payment;
import com.oheat.order.exception.DuplicatePaymentKeyException;
import com.oheat.order.exception.InvalidPaymentInfoException;
import com.oheat.order.repository.PaymentRepository;
import com.oheat.order.service.TossPaymentService;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PaymentServiceTest {

    private final PaymentRepository paymentRepository = new MemoryPaymentRepository();
    private TossPaymentService tossPaymentService;

    @BeforeEach
    void setUp() {
        tossPaymentService = new TossPaymentService(paymentRepository);
    }

    @Test
    @DisplayName("클라이언트로부터 받은 orderId(UUID), paymentKey, amount 정보를 저장한다.")
    void whenReceiveOrderIdAndPaymentKeyAndAmountFromClient_thenSaveSuccess() {
        Payment paymentInfo = Payment.builder()
            .orderId(UUID.randomUUID())
            .amount(50_000)
            .paymentKey("tgen_20250102210202h9Oy0")
            .build();

        Assertions.assertDoesNotThrow(() -> {
            tossPaymentService.savePaymentInfo(paymentInfo);
        });
    }

    @Test
    @DisplayName("클라이언트로부터 받은 orderId(UUID) 가 이미 존재하면 실패한다.")
    void whenReceiveDuplicateOrderId_thenThrowDuplicateOrderIdException() {
        Payment payment = Payment.builder()
            .orderId(UUID.randomUUID())
            .amount(50_000)
            .paymentKey("tgen_20250102210202h9Oy0")
            .build();
        paymentRepository.save(payment);

        Assertions.assertThrows(DuplicatePaymentKeyException.class, () -> {
            tossPaymentService.savePaymentInfo(payment);
        });
    }

    @Disabled
    @Test
    @DisplayName("구매자에 의해 결제가 취소(PAY_PROCESS_CANCELED)되어 failUrl로 응답이 오면 실패 사유와 함께 400을 반환한다.")
    void test2() {

    }

    @Disabled
    @Test
    @DisplayName("결제가 실패(PAY_PROCESS_ABORTED)하여 failUrl로 응답이 오면 실패 사유와 함께 400을 반환한다.")
    void test3() {

    }

    @Disabled
    @Test
    @DisplayName("구매자의 카드 정보 문제(REJECT_CARD_COMPANY)로 인해 failUrl로 응답이 오면 실패 사유와 함께 403 반환한다.")
    void test4() {

    }

    @Test
    @DisplayName("결제 정보 검증에 실패하면, InvalidPaymentInfoException(400에러)를 반환한다.")
    void givenInvalidPaymentInfo_whenValidatePaymentInfo_thenThrowInvalidPaymentInfoException() {
        UUID uuid1 = UUID.fromString("9d37d6f7-842e-4d73-803f-5220283ae116");
        UUID uuid2 = UUID.fromString("1aaaaaaa-842e-4d73-803f-5220283ae116");

        Payment paymentFromClient = Payment.builder()
            .orderId(uuid1)
            .amount(50_000)
            .paymentKey("tgen_20250102210202h9Oy0")
            .build();
        paymentRepository.save(paymentFromClient);

        Payment paymentFromTossPayments = Payment.builder()
            .orderId(uuid2)
            .amount(50_000)
            .paymentKey("tgen_20250102210202h9Oy0")
            .build();

        Assertions.assertThrows(InvalidPaymentInfoException.class, () -> {
            tossPaymentService.confirm(paymentFromTossPayments);
        });
    }

    @Test
    @DisplayName("클라이언트와 토스페이먼츠로부터 받은 결제 정보를 비교하여 같은지 검증하고 성공한다.")
    void whenValidatePaymentInfo_thenSuccess() {
        UUID uuid = UUID.randomUUID();

        Payment paymentFromClient = Payment.builder()
            .orderId(uuid)
            .amount(50_000)
            .paymentKey("tgen_20250102210202h9Oy0")
            .build();
        paymentRepository.save(paymentFromClient);

        Payment paymentFromTossPayments = Payment.builder()
            .orderId(uuid)
            .amount(50_000)
            .paymentKey("tgen_20250102210202h9Oy0")
            .build();

        Assertions.assertDoesNotThrow(() -> {
            tossPaymentService.confirm(paymentFromTossPayments);
        });
    }

    @Disabled
    @Test
    @DisplayName("결제 정보 검증에 성공하면, 토스페이먼츠 결제 승인 API에 요청을 보낸다.")
    void test7() {

    }

    @Disabled
    @Test
    @DisplayName("결제 승인 요청의 문제로 NOT_FOUND_PAYMENT_SESSION 에러 발생하면, 404를 반환한다.")
    void test8() {

    }

    @Disabled
    @Test
    @DisplayName("결제 승인 요청 시, 카드사에서 해당 카드를 거절하여 REJECT_CARD_COMPANY 에러 발생하면, 403을 반환한다.")
    void test9() {

    }

    @Disabled
    @Test
    @DisplayName("결제 승인 요청 시, API 키값 또는 주문번호가 최초 요청 값과 달라 FORBIDDEN_REQUEST 에러 발생하면, 403을 반환한다.")
    void test10() {

    }

    @Disabled
    @Test
    @DisplayName("결제 승인 요청 시, API 키를 잘못 입력하여 UNAUTHORIZED_KEY 에러 발생하면, 401을 반환한다.")
    void test11() {

    }

    @Disabled
    @Test
    @DisplayName("결제 승인에 성공했지만, 주문 정보를 저장하는 과정에서 문제 발생 시 롤백하고 500을 반환한다.")
    void test12() {

    }

    @Disabled
    @Test
    @DisplayName("결제 승인에 성공하면, 주문 정보를 저장하고 200을 반환한다.")
    void test13() {

    }
}
