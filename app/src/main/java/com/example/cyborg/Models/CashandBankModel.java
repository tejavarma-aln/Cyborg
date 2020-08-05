package com.example.cyborg.Models;

public class CashandBankModel {

    private  String entityName;
    private Boolean isLedgerEntity;
    private String entityDebitAmount;
    private String entityCreditAmount;


    public CashandBankModel(String entityName, Boolean isLedgerEntity, String entityDebitAmount, String entityCreditAmount) {
        this.entityName = entityName;
        this.isLedgerEntity = isLedgerEntity;
        this.entityDebitAmount = entityDebitAmount;
        this.entityCreditAmount = entityCreditAmount;
    }

    public String getEntityName() {
        return entityName;
    }

    public Boolean getLedgerEntity() {
        return isLedgerEntity;
    }

    public String getEntityDebitAmount() {
        return entityDebitAmount;
    }

    public String getEntityCreditAmount() {
        return entityCreditAmount;
    }
}
