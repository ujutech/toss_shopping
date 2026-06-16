package com.toss.shopping.common;

import com.google.gson.annotations.SerializedName;

public class TossApiError {

    @SerializedName("errorCode")
    private String errorCode;

    @SerializedName("reason")
    private String reason;

    public String getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "TossApiError{errorCode='" + errorCode + "', reason='" + reason + "'}";
    }
}
