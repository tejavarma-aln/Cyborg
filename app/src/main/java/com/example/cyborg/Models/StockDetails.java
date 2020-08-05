package com.example.cyborg.Models;

public class StockDetails {

        private String itemQuantity;
        private String itemRate;
        private String itemAmount;

        public StockDetails(String itemQuantity, String itemRate, String itemAmount) {
            this.itemQuantity = itemQuantity;
            this.itemRate = itemRate;
            this.itemAmount = itemAmount;
        }

        public String getItemQuantity() {
            return itemQuantity;
        }

        public String getItemRate() {
            return itemRate;
        }

        public String getItemAmount() {
            return itemAmount;
        }
    }


