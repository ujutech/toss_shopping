package com.toss.shopping.controller;

import com.toss.shopping.apiclient.TossAuthClient;
import com.toss.shopping.dto.auth.TossTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Auth", description = "토스 인증 API")
@RestController
@RequestMapping("/api/auth")
public class TossAuthController {

    private final TossAuthClient authClient;

    public TossAuthController(TossAuthClient authClient) {
        this.authClient = authClient;
    }

    @Operation(summary = "액세스 토큰 발급")
    @GetMapping("/token")
    public TossTokenResponse getToken() throws IOException {
        return authClient.getAccessToken();
    }
}
