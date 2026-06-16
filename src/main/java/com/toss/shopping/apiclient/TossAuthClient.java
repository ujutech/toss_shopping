package com.toss.shopping.apiclient;

import com.google.gson.Gson;
import com.toss.shopping.dto.auth.TossTokenResponse;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TossAuthClient {

    private static final String TOKEN_URL = "https://oauth2.cert.toss.im/token";
    private static final String SCOPE = "toss-shopping-fep:write";
    private static final long TOKEN_VALID_MILLIS = 59 * 60 * 1000L;

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String clientId;
    private final String clientSecret;

    private TossTokenResponse cachedToken;
    private long tokenExpiryTime;

    public TossAuthClient(OkHttpClient httpClient,
                          @Value("${toss.client-id}") String clientId,
                          @Value("${toss.client-secret}") String clientSecret) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("환경변수 TOSS_CLIENT_ID 가 설정되지 않았습니다.");
        }
        if (clientSecret == null || clientSecret.isBlank()) {
            throw new IllegalArgumentException("환경변수 TOSS_CLIENT_SECRET 이 설정되지 않았습니다.");
        }
        this.httpClient = httpClient;
        this.gson = new Gson();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public synchronized void invalidateToken() {
        cachedToken = null;
        tokenExpiryTime = 0;
    }

    public synchronized TossTokenResponse getAccessToken() throws IOException {
        if (cachedToken == null || System.currentTimeMillis() >= tokenExpiryTime) {
            cachedToken = fetchToken();
            tokenExpiryTime = System.currentTimeMillis() + TOKEN_VALID_MILLIS;
        }
        return cachedToken;
    }

    private TossTokenResponse fetchToken() throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("scope", SCOPE)
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .header("Accept", "application/json; charset=UTF-8")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                throw new IOException("토스 인증 실패 - HTTP " + response.code() + ": " + responseBody);
            }

            return gson.fromJson(responseBody, TossTokenResponse.class);
        }
    }
}
