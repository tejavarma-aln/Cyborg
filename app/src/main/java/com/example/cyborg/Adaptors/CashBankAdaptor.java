package com.example.cyborg.Adaptors;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Models.CashandBankModel;
import com.example.cyborg.R;

import java.util.ArrayList;

public class CashBankAdaptor extends RecyclerView.Adapter<CashBankAdaptor.CashBankViewHolder> {


    private ArrayList<CashandBankModel> cashandBankModels;

     public CashBankAdaptor(ArrayList<CashandBankModel> cashandBankModels){
         this.cashandBankModels = cashandBankModels;
     }

     public void updateAdapter(ArrayList<CashandBankModel> data){
         cashandBankModels.clear();
         cashandBankModels.addAll(data);
         notifyDataSetChanged();
     }


    @NonNull
    @Override
    public CashBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_bank_view_holder,parent,false);
        return new CashBankViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CashBankViewHolder holder, int position) {
        CashandBankModel cashandBankModel = cashandBankModels.get(position);
         boolean isLedger = cashandBankModel.getLedgerEntity();
         boolean isBankGroup = cashandBankModel.getEntityName().toLowerCase().contains("bank");
         int ImageId = isBankGroup?R.drawable.ic_baseline_account_balance_24:R.drawable.ic_baseline_attach_money_24;
         holder.entityName.setText(cashandBankModel.getEntityName());
         holder.entityName.setTextSize(TypedValue.COMPLEX_UNIT_SP,isLedger?16:20);
         holder.image.setImageResource(ImageId);
         holder.entityDebitAmount.setText(cashandBankModel.getEntityDebitAmount());
         holder.entityCreditAmount.setText(cashandBankModel.getEntityCreditAmount());

    }

    @Override
    public int getItemCount() {
        return cashandBankModels.size();
    }

    public static class CashBankViewHolder extends RecyclerView.ViewHolder{

         private AppCompatImageView image;
         private AppCompatTextView entityName;
         private AppCompatTextView entityDebitAmount;
         private AppCompatTextView entityCreditAmount;

        public CashBankViewHolder(View v){
            super(v);
            image =v.findViewById(R.id.entityImage);
            entityName = v.findViewById(R.id.entityName);
            entityCreditAmount = v.findViewById(R.id.entityCreditAmount);
            entityDebitAmount = v.findViewById(R.id.entityDebitAmount);
        }

    }
}
