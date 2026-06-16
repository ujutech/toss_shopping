package com.toss.shopping.dto.product;

import com.google.gson.annotations.SerializedName;

public class TossProduct {

    @SerializedName("id")
    private long id;

    public long getId() {
        return id;
    }
}
