package com.oheat.order.service;

import com.oheat.order.dto.TossPaymentConfirmResponse;
import com.oheat.order.entity.Payment;
import com.oheat.order.exception.TossPaymentConfirmException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentConfirmClient {

    private final RestClient restClient = RestClient.create();
    @Value("${spring.payment.toss.secret-key}")
    private String secretKey;
    @Value("${spring.payment.toss.confirm-url}")
    private String confirmUrl;

    public ResponseEntity<TossPaymentConfirmResponse> confirmTossPayment(Payment payment) {

        Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorization = "Basic " + new String(encodedBytes);

        return restClient.post()
            .uri(confirmUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", authorization)
            .body(payment)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::errorhandler4xx)
            .onStatus(HttpStatusCode::is5xxServerError, this::errorhandler5xx)
            .toEntity(TossPaymentConfirmResponse.class);
    }

    private void errorhandler4xx(HttpRequest request, ClientHttpResponse response)
        throws IOException {
        throw new TossPaymentConfirmException(response.getStatusCode(), "결제 승인에 실패했습니다.");
    }

    private void errorhandler5xx(HttpRequest request, ClientHttpResponse response)
        throws IOException {
        throw new TossPaymentConfirmException(response.getStatusCode(), "결제 서버 오류로 결제 승인에 실패했습니다.");
    }
}
