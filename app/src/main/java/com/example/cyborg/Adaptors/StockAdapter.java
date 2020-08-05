package com.example.cyborg.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Models.StockDetails;
import com.example.cyborg.Models.StockModel;
import com.example.cyborg.R;

import java.util.ArrayList;


public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> implements Filterable {

    private ArrayList<StockModel> stockAdapter;
    private ArrayList<StockModel> stockAdapterAll;

    public StockAdapter(ArrayList<StockModel> adapters){
        this.stockAdapter = adapters;
        this.stockAdapterAll = new ArrayList<>(adapters);
    }


    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_view_holder,parent,false);
        return new StockViewHolder(v);
    }


    public void updateAdapter(ArrayList<StockModel> data){
        stockAdapterAll.clear();
        stockAdapter.clear();
        stockAdapter.addAll(data);
        stockAdapterAll.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {

        StockModel stockmodel = stockAdapter.get(position);
        StockDetails closing = stockmodel.getItemClosing();

        holder.itemName.setText( stockmodel.getItemName());
        holder.itemClosingQty.setText(String.format("%s : %s","Closing Qty",closing.getItemQuantity()));
        holder.itemClosingRate.setText(String.format("%s : %s","Closing Rate",closing.getItemRate()));
        holder.itemClosingAmount.setText(closing.getItemAmount());

    }

    @Override

    public int getItemCount() {
        return stockAdapter.size();
    }

    @Override
    public Filter getFilter() {
        return stockFilter;
    }

    Filter stockFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<StockModel> temp = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                temp.addAll(stockAdapterAll);
            }else{
                String pattern = constraint.toString().trim().toLowerCase();
                for(StockModel stockModel : stockAdapterAll){
                    if(stockModel.getItemName().toLowerCase().contains(pattern)){
                        temp.add(stockModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = temp;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            stockAdapter.clear();
            stockAdapter.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


    public static class StockViewHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView itemName;
        private AppCompatTextView itemClosingQty;
        private AppCompatTextView itemClosingRate;
        private AppCompatTextView itemClosingAmount;

        public StockViewHolder(View v){
            super(v);
            itemName = v.findViewById(R.id.itemName);
            itemClosingQty = v.findViewById(R.id.itemClosingQty);
            itemClosingRate = v.findViewById(R.id.itemClosingRate);
            itemClosingAmount = v.findViewById(R.id.itemClosingAmount);

        }

    }
}
