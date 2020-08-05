package com.example.cyborg.Models;

public class OutstandingModel {

    private String voucherDate;
    private String voucherRef;
    private String ledgerName;
    private String balanceAmount;
    private String balanceDueOn;
    private String balanceOverDue;

    public OutstandingModel(String voucherDate, String voucherRef, String ledgerName, String balanceAmount, String balanceDueOn, String balanceOverDue) {
        this.voucherDate = voucherDate;
        this.voucherRef = voucherRef;
        this.ledgerName = ledgerName;
        this.balanceAmount = balanceAmount;
        this.balanceDueOn = balanceDueOn;
        this.balanceOverDue = balanceOverDue;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public String getVoucherRef() {
        return voucherRef;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public String getBalanceDueOn() {
        return balanceDueOn;
    }

    public String getBalanceOverDue() {
        return balanceOverDue;
    }
}
