package com.toss.shopping.service;

import com.toss.shopping.apiclient.TossProductClient;
import com.toss.shopping.dto.product.TossProduct;
import com.toss.shopping.dto.product.TossProductRemoveAllResponse;
import com.toss.shopping.dto.product.TossProductRemoveResponse;
import com.toss.shopping.dto.product.TossProductResponse;
import com.toss.shopping.dto.product.TossProductSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TossProductService {

    private static final Logger log = LoggerFactory.getLogger(TossProductService.class);

    private final TossProductClient productClient;

    public TossProductService(TossProductClient productClient) {
        this.productClient = productClient;
    }

    public TossProductResponse getProducts(String nextToken) throws IOException {
        return productClient.getProducts(nextToken);
    }

    public TossProductRemoveResponse removeProduct(long productId, String partnerName) throws IOException {
        return productClient.removeProduct(productId, partnerName);
    }

    public TossProductRemoveAllResponse removeAllProducts(String partnerName) throws IOException {
        log.info("전체 상품 삭제 시작 (partnerName: {})", partnerName);
        int successCount = 0;
        int failCount = 0;
        String nextToken = null;
        boolean hasNext = true;

        while (hasNext) {
            TossProductResponse response = productClient.getProducts(nextToken);
            TossProductSuccess success = response.getSuccess();

            log.info("상품 페이지 처리 중 - 상품 수: {}, hasNext: {}", success.getProducts().size(), success.isHasNext());

            for (TossProduct product : success.getProducts()) {
                TossProductRemoveResponse removeResponse = productClient.removeProduct(product.getId(), partnerName);
                if (removeResponse.isSuccess()) {
                    successCount++;
                    log.debug("상품 삭제 성공 (productId: {})", product.getId());
                } else {
                    failCount++;
                    log.warn("상품 삭제 실패 (productId: {})", product.getId());
                }
            }

            hasNext = success.isHasNext();
            nextToken = success.getNextToken();
        }

        log.info("전체 상품 삭제 완료 - 성공: {}, 실패: {}", successCount, failCount);
        return new TossProductRemoveAllResponse(successCount, failCount);
    }
}
