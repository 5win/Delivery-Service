package com.oheat.order.service;

import com.oheat.order.dto.TossPaymentConfirmResponse;
import com.oheat.order.entity.Payment;
import com.oheat.order.exception.PaymentCannotCancelException;
import com.oheat.order.exception.PaymentCannotConfirmException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class TossPaymentClient {

    private final RestClient restClient;
    @Value("${spring.payment.toss.secret-key}")
    private String secretKey;
    @Value("${spring.payment.toss.base-url}")
    private String baseUrl;

    public ResponseEntity<TossPaymentConfirmResponse> confirmPayment(Payment payment) {
        Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorization = "Basic " + new String(encodedBytes);

        return restClient.post()
            .uri(baseUrl + "/confirm")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", authorization)
            .body(payment)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::confirm4xxErrorHandler)
            .onStatus(HttpStatusCode::is5xxServerError, this::confirm5xxErrorHandler)
            .toEntity(TossPaymentConfirmResponse.class);
    }

    public ResponseEntity<Void> cancelPayment(@NonNull String paymentKey, @NonNull String cancelReason) {
        Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorization = "Basic " + new String(encodedBytes);

        Map<String, String> cancelReasonMap = Map.of("cancelReason", cancelReason);

        return restClient.post()
            .uri(baseUrl + "/" + paymentKey + "/cancel")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", authorization)
            .body(cancelReasonMap)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::cancel4xxErrorHandler)
            .onStatus(HttpStatusCode::is5xxServerError, this::cancel5xxErrorHandler)
            .toBodilessEntity();
    }

    private void confirm4xxErrorHandler(HttpRequest request, ClientHttpResponse response)
        throws IOException {
        throw new PaymentCannotConfirmException(response.getStatusCode(), "결제 승인에 실패했습니다.");
    }

    private void confirm5xxErrorHandler(HttpRequest request, ClientHttpResponse response)
        throws IOException {
        throw new PaymentCannotConfirmException(response.getStatusCode(), "결제 서버 오류로 결제 승인에 실패했습니다.");
    }

    private void cancel4xxErrorHandler(HttpRequest request, ClientHttpResponse response)
        throws IOException {
        throw new PaymentCannotCancelException(response.getStatusCode(), "결제 취소에 실패했습니다.");
    }

    private void cancel5xxErrorHandler(HttpRequest request, ClientHttpResponse response)
        throws IOException {
        throw new PaymentCannotCancelException(response.getStatusCode(), "결제 서버 오류로 결제 취소에 실패했습니다.");
    }
}
