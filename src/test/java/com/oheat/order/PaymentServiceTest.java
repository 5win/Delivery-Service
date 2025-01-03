package com.oheat.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.oheat.order.dto.TossPaymentResponse;
import com.oheat.order.entity.Payment;
import com.oheat.order.exception.DuplicatePaymentKeyException;
import com.oheat.order.exception.InvalidPaymentInfoException;
import com.oheat.order.exception.TossPaymentConfirmException;
import com.oheat.order.repository.PaymentRepository;
import com.oheat.order.service.PaymentConfirmClient;
import com.oheat.order.service.TossPaymentService;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PaymentServiceTest {

    private final PaymentRepository paymentRepository = new MemoryPaymentRepository();
    private final PaymentConfirmClient paymentConfirmClient = Mockito.mock(PaymentConfirmClient.class);
    private TossPaymentService tossPaymentService;

    @BeforeEach
    void setUp() {
        tossPaymentService = new TossPaymentService(paymentRepository, paymentConfirmClient);
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

    @Test
    @DisplayName("결제 정보 검증에 성공하면, 토스페이먼츠 결제 승인 API에 요청을 보내고 200 응답과 정보를 받는다.")
    void whenPaymentInfoValid_thenRequestConfirmApiAndSuccess() {
        UUID uuid = UUID.randomUUID();
        String paymentKey = "tgen_20250102210202h9Oy0";

        Payment payment = Payment.builder()
            .orderId(uuid)
            .amount(50_000)
            .paymentKey(paymentKey)
            .build();
        paymentRepository.save(payment);

        when(paymentConfirmClient.confirmTossPayment(any()))
            .thenReturn(ResponseEntity.ok(TossPaymentResponse.builder()
                .orderId(uuid)
                .paymentKey(paymentKey)
                .build()));

        ResponseEntity<TossPaymentResponse> result = tossPaymentService.confirm(payment);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getOrderId()).isEqualTo(uuid);
        assertThat(result.getBody().getPaymentKey()).isEqualTo(paymentKey);
    }

    @Test
    @DisplayName("결제 승인 요청의 문제로 NOT_FOUND_PAYMENT_SESSION 에러 발생하면, 404를 반환한다.")
    void whenConfirmResponseNotFoundPaymentSession_thenReceive404() {
        UUID uuid = UUID.randomUUID();
        String paymentKey = "tgen_20250102210202h9Oy0";

        Payment payment = Payment.builder()
            .orderId(uuid)
            .amount(50_000)
            .paymentKey(paymentKey)
            .build();
        paymentRepository.save(payment);

        when(paymentConfirmClient.confirmTossPayment(any()))
            .thenThrow(new TossPaymentConfirmException(HttpStatus.NOT_FOUND, "결제 승인에 실패했습니다."));

        Assertions.assertThrows(TossPaymentConfirmException.class, () -> {
            tossPaymentService.confirm(payment);
        });
    }

    @Test
    @DisplayName("결제 승인 요청 시, forbidden 에러 발생하면, 403을 반환한다.")
    void whenConfirmResponseForbidden_thenReceive403() {
        UUID uuid = UUID.randomUUID();
        String paymentKey = "tgen_20250102210202h9Oy0";

        Payment payment = Payment.builder()
            .orderId(uuid)
            .amount(50_000)
            .paymentKey(paymentKey)
            .build();
        paymentRepository.save(payment);

        when(paymentConfirmClient.confirmTossPayment(any()))
            .thenThrow(new TossPaymentConfirmException(HttpStatus.FORBIDDEN, "결제 승인에 실패했습니다."));

        Assertions.assertThrows(TossPaymentConfirmException.class, () -> {
            tossPaymentService.confirm(payment);
        });
    }

    @Test
    @DisplayName("결제 승인 요청 시, API 키를 잘못 입력하여 UNAUTHORIZED_KEY 에러 발생하면, 401을 반환한다.")
    void whenConfirmResponseUnauthorizedKey_thenReceive401() {
        UUID uuid = UUID.randomUUID();
        String paymentKey = "tgen_20250102210202h9Oy0";

        Payment payment = Payment.builder()
            .orderId(uuid)
            .amount(50_000)
            .paymentKey(paymentKey)
            .build();
        paymentRepository.save(payment);

        when(paymentConfirmClient.confirmTossPayment(any()))
            .thenThrow(new TossPaymentConfirmException(HttpStatus.UNAUTHORIZED, "결제 승인에 실패했습니다."));

        Assertions.assertThrows(TossPaymentConfirmException.class, () -> {
            tossPaymentService.confirm(payment);
        });
    }

    @Test
    @DisplayName("결제 승인 요청 시, PG 서버 내부 오류로 Server Internal Error가 발생하면, 500을 반환한다.")
    void whenConfirmResponseInternalError_thenReceive500() {
        UUID uuid = UUID.randomUUID();
        String paymentKey = "tgen_20250102210202h9Oy0";

        Payment payment = Payment.builder()
            .orderId(uuid)
            .amount(50_000)
            .paymentKey(paymentKey)
            .build();
        paymentRepository.save(payment);

        when(paymentConfirmClient.confirmTossPayment(any()))
            .thenThrow(new TossPaymentConfirmException(HttpStatus.INTERNAL_SERVER_ERROR, "결제 서버 오류로 결제 승인에 실패했습니다."));

        Assertions.assertThrows(TossPaymentConfirmException.class, () -> {
            tossPaymentService.confirm(payment);
        });
    }

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
