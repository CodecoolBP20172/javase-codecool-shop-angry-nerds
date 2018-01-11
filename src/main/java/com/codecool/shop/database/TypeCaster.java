package com.codecool.shop.database;

public class TypeCaster {

    private String content;
    private boolean isNumber;
    public TypeCaster(String content, boolean isNumber){
        this.content = content;
        this.isNumber = isNumber;
    }

    public String getContent() {
        return content;
    }

    public boolean isNumber() {
        return isNumber;
    }
}
