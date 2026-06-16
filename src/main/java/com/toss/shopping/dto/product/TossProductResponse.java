package com.toss.shopping.dto.product;

import com.google.gson.annotations.SerializedName;

public class TossProductResponse {

    @SerializedName("resultType")
    private String resultType;

    @SerializedName("success")
    private TossProductSuccess success;

    public String getResultType() {
        return resultType;
    }

    public TossProductSuccess getSuccess() {
        return success;
    }
}
