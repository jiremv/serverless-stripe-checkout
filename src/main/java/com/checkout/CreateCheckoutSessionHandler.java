package com.checkout;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CreateCheckoutSessionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Moshi moshi = new Moshi.Builder().build();
    private static final Type mapType = Types.newParameterizedType(Map.class, String.class, String.class);
    private static final JsonAdapter<Map<String, String>> jsonAdapter = moshi.adapter(mapType);

    static {
        Stripe.apiKey = System.getenv("STRIPE_API_KEY");
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://example.com/success")
                    .setCancelUrl("https://example.com/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(2000L) // $20.00
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Producto ejemplo")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("checkoutUrl", session.getUrl());

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(jsonAdapter.toJson(responseBody))
                    .build();

        } catch (StripeException e) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody("Error creando sesión: " + e.getMessage())
                    .build();
        }
    }
}