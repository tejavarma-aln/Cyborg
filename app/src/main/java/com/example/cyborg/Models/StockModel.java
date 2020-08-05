package com.example.cyborg.Models;

public class StockModel {

    private String itemName;
    private StockDetails itemClosing;

    public StockModel(String itemName,StockDetails itemClosing) {
        this.itemName = itemName;
        this.itemClosing = itemClosing;
    }

    public String getItemName() {
        return itemName;
    }

    public StockDetails getItemClosing() {
        return itemClosing;
    }
}
