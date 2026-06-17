package com.toss.shopping.apiclient;

import com.google.gson.Gson;
import com.toss.shopping.dto.product.TossProductRemoveRequest;
import com.toss.shopping.dto.product.TossProductRemoveResponse;
import com.toss.shopping.dto.product.TossProductResponse;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

@Component
public class TossProductClient {

    private static final Logger log = LoggerFactory.getLogger(TossProductClient.class);

    private static final String PRODUCTS_URL = "https://shopping-fep.toss.im/api/v3/shopping-fep/products/v2";
    private static final String PRODUCTS_REMOVE_URL = "https://shopping-fep.toss.im/api/v3/shopping-fep/products/remove";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final TossAuthClient authClient;

    public TossProductClient(OkHttpClient httpClient, TossAuthClient authClient) {
        this.httpClient = httpClient;
        this.gson = new Gson();
        this.authClient = authClient;
    }

    public TossProductResponse getProducts(String nextToken) throws IOException {
        log.debug("상품 목록 조회 요청 (nextToken: {})", nextToken);
        return executeWithRetry(token -> {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(PRODUCTS_URL).newBuilder();
            if (nextToken != null && !nextToken.isBlank()) {
                urlBuilder.addQueryParameter("nextToken", nextToken);
            }
            return new Request.Builder()
                    .url(urlBuilder.build())
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json; charset=UTF-8")
                    .get()
                    .build();
        }, TossProductResponse.class, "상품 목록 조회 실패");
    }

    public TossProductRemoveResponse removeProduct(long productId, String partnerName) throws IOException {
        log.debug("상품 삭제 요청 (productId: {}, partnerName: {})", productId, partnerName);
        String json = gson.toJson(new TossProductRemoveRequest(productId, partnerName));
        return executeWithRetry(token -> new Request.Builder()
                .url(PRODUCTS_REMOVE_URL)
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json; charset=UTF-8")
                .post(RequestBody.create(json, JSON))
                .build(),
                TossProductRemoveResponse.class, "상품 삭제 실패");
    }

    private <T> T executeWithRetry(Function<String, Request> requestBuilder, Class<T> responseType, String errorMsg) throws IOException {
        String token = authClient.getAccessToken().getAccessToken();

        try (Response response = httpClient.newCall(requestBuilder.apply(token)).execute()) {
            if (response.code() == 401) {
                log.warn("401 응답 수신 - 토큰 갱신 후 재시도");
                authClient.invalidateToken();
                return execute(requestBuilder.apply(authClient.getAccessToken().getAccessToken()), responseType, errorMsg);
            }
            return parse(response, responseType, errorMsg);
        }
    }

    private <T> T execute(Request request, Class<T> responseType, String errorMsg) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            return parse(response, responseType, errorMsg);
        }
    }

    private <T> T parse(Response response, Class<T> responseType, String errorMsg) throws IOException {
        String body = response.body() != null ? response.body().string() : "";
        if (!response.isSuccessful()) {
            log.error("{} - HTTP {}: {}", errorMsg, response.code(), body);
            throw new IOException(errorMsg + " - HTTP " + response.code() + ": " + body);
        }
        return gson.fromJson(body, responseType);
    }
}
