package com.toss.shopping.service;

import com.toss.shopping.apiclient.TossProductClient;
import com.toss.shopping.dto.product.TossProduct;
import com.toss.shopping.dto.product.TossProductRemoveAllResponse;
import com.toss.shopping.dto.product.TossProductRemoveResponse;
import com.toss.shopping.dto.product.TossProductResponse;
import com.toss.shopping.dto.product.TossProductSuccess;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TossProductService {

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
        int successCount = 0;
        int failCount = 0;
        String nextToken = null;
        boolean hasNext = true;

        while (hasNext) {
            TossProductResponse response = productClient.getProducts(nextToken);
            TossProductSuccess success = response.getSuccess();

            for (TossProduct product : success.getProducts()) {
                TossProductRemoveResponse removeResponse = productClient.removeProduct(product.getId(), partnerName);
                if (removeResponse.isSuccess()) {
                    successCount++;
                } else {
                    failCount++;
                }
            }

            hasNext = success.isHasNext();
            nextToken = success.getNextToken();
        }

        return new TossProductRemoveAllResponse(successCount, failCount);
    }
}
