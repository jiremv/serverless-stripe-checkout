package com.checkout;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.squareup.moshi.Moshi;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import java.util.Base64;
import java.util.Map;

public class StripeWebhookHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Moshi moshi = new Moshi.Builder().build();
    private static final String STRIPE_WEBHOOK_SECRET = System.getenv("STRIPE_WEBHOOK_SECRET");

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            Map<String, String> headers = (Map<String, String>) input.get("headers");
            String payload = (String) input.get("body");
            String sigHeader = headers.get("stripe-signature");

            Event event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    STRIPE_WEBHOOK_SECRET
            );

            context.getLogger().log("Evento recibido: " + event.getType());

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody("Evento procesado: " + event.getType())
                    .build();

        } catch (SignatureVerificationException e) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(400)
                    .setObjectBody("Firma inválida: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody("Error procesando webhook: " + e.getMessage())
                    .build();
        }
    }
}
