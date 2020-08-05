package com.example.cyborg.Models;

public class LedgerModel {

    public LedgerModel(String ledgerName, String ledgerClosing) {
        this.ledgerName = ledgerName;
        this.ledgerClosing = ledgerClosing;
    }

    public String ledgerName;
    public String ledgerClosing;

    public String getLedgerName() {
        return ledgerName;
    }

    public String getLedgerClosing() {
        return ledgerClosing;
    }
}
