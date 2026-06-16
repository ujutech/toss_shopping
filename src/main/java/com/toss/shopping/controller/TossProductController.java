package com.toss.shopping.controller;

import com.toss.shopping.dto.product.TossProductRemoveAllResponse;
import com.toss.shopping.dto.product.TossProductRemoveResponse;
import com.toss.shopping.dto.product.TossProductResponse;
import com.toss.shopping.service.TossProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Product", description = "토스 상품 API")
@RestController
@RequestMapping("/api/products")
public class TossProductController {

    private final TossProductService productService;

    public TossProductController(TossProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 목록 조회")
    @GetMapping
    public TossProductResponse getProducts(
            @Parameter(description = "다음 페이지 토큰") @RequestParam(required = false) String nextToken
    ) throws IOException {
        return productService.getProducts(nextToken);
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{id}")
    public TossProductRemoveResponse removeProduct(
            @Parameter(description = "상품 ID") @PathVariable long id,
            @Parameter(description = "파트너명") @RequestParam String partnerName
    ) throws IOException {
        return productService.removeProduct(id, partnerName);
    }

    @Operation(summary = "전체 상품 삭제")
    @DeleteMapping("/all")
    public TossProductRemoveAllResponse removeAllProducts(
            @Parameter(description = "파트너명") @RequestParam String partnerName
    ) throws IOException {
        return productService.removeAllProducts(partnerName);
    }
}
