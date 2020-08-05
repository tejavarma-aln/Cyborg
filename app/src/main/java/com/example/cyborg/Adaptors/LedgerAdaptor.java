package com.example.cyborg.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Models.LedgerModel;
import com.example.cyborg.R;
import com.example.cyborg.Interface.OnledgerClickInterface;

import java.util.ArrayList;

public class LedgerAdaptor extends RecyclerView.Adapter<LedgerAdaptor.LedgerViewHolder> implements Filterable {

     private ArrayList<LedgerModel> ledgerModels;
     private ArrayList<LedgerModel> ledgerModelsAll;
     private OnledgerClickInterface onledgerClickInterface;

    public LedgerAdaptor(ArrayList<LedgerModel> ledgerModels,OnledgerClickInterface onledgerClickInterface) {
        this.ledgerModels = ledgerModels;
        this.ledgerModelsAll = new ArrayList<>(ledgerModels);
        this.onledgerClickInterface =  onledgerClickInterface;
    }

    @NonNull
    @Override
    public LedgerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_view_holder,parent,false);
        return (new LedgerViewHolder(v,onledgerClickInterface));
    }

    @Override
    public void onBindViewHolder(@NonNull LedgerViewHolder holder, int position) {

        LedgerModel ledgerModel = ledgerModels.get(position);
        holder.ledgerName.setText(ledgerModel.getLedgerName());
        holder.ledgerClosing.setText(String.format("Closing Balance : %s",ledgerModel.getLedgerClosing()));
    }

    @Override
    public int getItemCount() {
        return ledgerModels.size();
    }

    @Override
    public Filter getFilter() {
        return  LedgerFilter;
    }

    /* filter for ledger   */

  private  Filter LedgerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<LedgerModel> ledgers = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                ledgers.addAll(ledgerModelsAll);
            }else{
                String pattern = constraint.toString().toLowerCase().trim();
                for(LedgerModel ledger : ledgerModelsAll){
                    if((ledger.ledgerName.toLowerCase().contains(pattern)) || (ledger.ledgerClosing.contains(pattern))){
                        ledgers.add(ledger);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = ledgers;
            return  filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ledgerModels.clear();
            ledgerModels.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };


    public  static  class  LedgerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private AppCompatTextView ledgerName;
        private AppCompatTextView ledgerClosing;
        private OnledgerClickInterface onledgerClickInterface;

        public LedgerViewHolder(View view,OnledgerClickInterface onledgerClickInterface){
            super(view);
            ledgerName = view.findViewById(R.id.ledgerName);
            ledgerClosing = view.findViewById(R.id.ledgerClosing);
            this.onledgerClickInterface = onledgerClickInterface;
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
                 AppCompatTextView ledgername = v.findViewById(R.id.ledgerName);
                 onledgerClickInterface.OnClick(ledgername.getText().toString());
        }
    }
}
