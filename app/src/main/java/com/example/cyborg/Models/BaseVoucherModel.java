package com.example.cyborg.Models;

public class BaseVoucherModel {

    private  String voucherName;
    private  String baseLedger;
    private  String voucherNumber;
    private  String voucherDate;
    private  String voucherAmount;


    public BaseVoucherModel(String voucherName, String baseLedger, String voucherNumber, String voucherDate, String voucherAmount) {
        this.voucherName = voucherName;
        this.baseLedger = baseLedger;
        this.voucherNumber = voucherNumber;
        this.voucherDate = voucherDate;
        this.voucherAmount = voucherAmount;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public String getBaseLedger() {
        return baseLedger;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public String getVoucherAmount() {
        return voucherAmount;
    }

}
