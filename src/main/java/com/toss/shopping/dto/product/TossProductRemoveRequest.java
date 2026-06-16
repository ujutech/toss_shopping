package com.toss.shopping.dto.product;

import com.google.gson.annotations.SerializedName;

public class TossProductRemoveRequest {

    @SerializedName("productId")
    private final long productId;

    @SerializedName("partnerName")
    private final String partnerName;

    public TossProductRemoveRequest(long productId, String partnerName) {
        this.productId = productId;
        this.partnerName = partnerName;
    }
}
