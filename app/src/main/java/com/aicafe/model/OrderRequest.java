package com.aicafe.model;

import java.util.List;

public class OrderRequest {
    private List<Integer> itemIds;

    public OrderRequest(List<Integer> itemIds) { this.itemIds = itemIds; }
}