package com.toss.shopping.dto.product;

public class TossProductRemoveAllResponse {

    private final int successCount;
    private final int failCount;

    public TossProductRemoveAllResponse(int successCount, int failCount) {
        this.successCount = successCount;
        this.failCount = failCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public int getTotalCount() {
        return successCount + failCount;
    }
}
