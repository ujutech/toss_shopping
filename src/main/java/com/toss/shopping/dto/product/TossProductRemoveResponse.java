package com.toss.shopping.dto.product;

import com.google.gson.annotations.SerializedName;
import com.toss.shopping.common.TossApiError;

public class TossProductRemoveResponse {

    @SerializedName("resultType")
    private String resultType;

    @SerializedName("error")
    private TossApiError error;

    public String getResultType() {
        return resultType;
    }

    public TossApiError getError() {
        return error;
    }

    public boolean isSuccess() {
        return "SUCCESS".equals(resultType);
    }
}
