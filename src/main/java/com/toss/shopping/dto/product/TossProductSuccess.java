package com.toss.shopping.dto.product;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TossProductSuccess {

    @SerializedName("products")
    private List<TossProduct> products;

    @SerializedName("nextToken")
    private String nextToken;

    @SerializedName("hasNext")
    private boolean hasNext;

    public List<TossProduct> getProducts() {
        return products;
    }

    public String getNextToken() {
        return nextToken;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
