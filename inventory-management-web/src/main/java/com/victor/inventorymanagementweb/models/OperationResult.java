package com.victor.inventorymanagementweb.models;

public enum OperationResult {
    ITEM_ADDED,
    INVALID_NAME,
    INVALID_PRICE,
    NEGATIVE_QUANTITY,
    QUANTITY_UPDATED,
    GENERAL_FAILURE,
    NOT_ENOUGH_STOCK,
    ITEM_SOLD,
    INVALID_QUANTITY,
    AMBIGUOUS,
    NOT_FOUND,
    OUT_OF_STOCK
}
