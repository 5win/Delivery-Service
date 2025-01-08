package com.oheat.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.oheat.order.dto.TossPaymentConfirmRequest;
import com.oheat.order.exception.PaymentCannotCancelException;
import com.oheat.order.exception.PaymentCannotConfirmException;
import com.oheat.order.service.TossPaymentClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

public class TossPaymentClientTest {

    private final RestClient.Builder restBuilder = RestClient.builder();
    private final MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restBuilder).build();

    private TossPaymentClient tossPaymentClient;

    @BeforeEach
    void setUp() {
        tossPaymentClient = new TossPaymentClient(restBuilder.build());

        ReflectionTestUtils.setField(tossPaymentClient, "secretKey", "test-key");
        ReflectionTestUtils.setField(tossPaymentClient, "baseUrl", "https://api.tosspayments.com/v1/payments");
    }

    @Test
    @DisplayName("토스페이먼츠 결제 승인에 성공하면, 200을 반환한다.")
    void whenTossSuccessPaymentConfirm_thenReturn200() {
        TossPaymentConfirmRequest confirmReq = TossPaymentConfirmRequest.builder().build();

        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
            .andRespond(withSuccess());

        var response = tossPaymentClient.confirmPayment(confirmReq);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("토스페이먼츠 결제 승인 요청에서 400 에러가 발생하면, 400을 반환한다.")
    void whenTossPaymentConfirm400Fail_thenReturn400() {
        TossPaymentConfirmRequest confirmReq = TossPaymentConfirmRequest.builder().build();

        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
            .andRespond(withBadRequest());

        var result = Assertions.assertThrows(
            PaymentCannotConfirmException.class, () ->
                tossPaymentClient.confirmPayment(confirmReq)
        );
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("토스페이먼츠 결제 승인 요청에서 500 에러가 발생하면, 500을 반환한다.")
    void whenTossPaymentConfirm500Fail_thenReturn500() {
        TossPaymentConfirmRequest confirmReq = TossPaymentConfirmRequest.builder().build();

        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
            .andRespond(withServerError());

        var result = Assertions.assertThrows(
            PaymentCannotConfirmException.class, () -> {
                tossPaymentClient.confirmPayment(confirmReq);
            });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("토스페이먼츠 결제 취소에 성공하면, 200을 반환한다.")
    void whenSuccessTossPaymentCancel_thenReturn200() {
        String paymentKey = "test-key";
        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
            .andRespond(withSuccess());

        var response = tossPaymentClient.cancelPayment(paymentKey, "cancelReason");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("토스페이먼츠 결제 취소 요청에서 400 에러가 발생하면, 400을 반환한다.")
    void whenTossPaymentCancelFail400_thenReturn400() {
        String paymentKey = "test-key";
        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
            .andRespond(withBadRequest());

        var result = Assertions.assertThrows(PaymentCannotCancelException.class, () -> {
            tossPaymentClient.cancelPayment(paymentKey, "cancelReason");
        });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("토스페이먼츠 결제 취소 요청에서 500 에러가 발생하면, 500을 반환한다.")
    void whenTossPaymentCancelFail500_thenReturn500() {
        String paymentKey = "test-key";
        mockRestServiceServer.expect(requestTo("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel"))
            .andRespond(withServerError());

        var result = Assertions.assertThrows(PaymentCannotCancelException.class, () -> {
            tossPaymentClient.cancelPayment(paymentKey, "cancelReason");
        });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}