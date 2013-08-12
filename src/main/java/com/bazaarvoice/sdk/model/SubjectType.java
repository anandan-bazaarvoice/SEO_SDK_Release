package com.bazaarvoice.sdk.model;

public enum SubjectType {
    PRODUCT,
    CATEGORY;

    public String uriValue() {
        return this.toString().toLowerCase();
    }
}
