package com.example.cyborg.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Models.BaseVoucherModel;
import com.example.cyborg.R;

import java.util.ArrayList;

public class BaseVoucherAdapter extends RecyclerView.Adapter<BaseVoucherAdapter.BaseVoucherViewHolder> implements Filterable {

    ArrayList<BaseVoucherModel> voucherModels;
    ArrayList<BaseVoucherModel> voucherModelsAll;

    public BaseVoucherAdapter(ArrayList<BaseVoucherModel> voucherModels) {
        this.voucherModels = voucherModels;
        this.voucherModelsAll = new ArrayList<>(voucherModels);
    }

    @NonNull
    @Override
    public BaseVoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_view_holder,parent,false);
        return (new BaseVoucherViewHolder(v));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseVoucherViewHolder holder, int position) {

          BaseVoucherModel baseVoucherModel = voucherModels.get(position);
          holder.baseName.setText(baseVoucherModel.getBaseLedger());
          holder.voucherAmount.setText(baseVoucherModel.getVoucherAmount());
          holder.voucherInfo.setText(String.format("%s | %s", baseVoucherModel.getVoucherDate(), baseVoucherModel.getVoucherNumber()));
          holder.voucherType.setText(String.format("%s : %s", "Type", baseVoucherModel.getVoucherName()));


    }

    public void updateAdapter(ArrayList<BaseVoucherModel> data){
        voucherModelsAll.clear();
        voucherModels.clear();
        voucherModels.addAll(data);
        voucherModelsAll.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return voucherModels.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<BaseVoucherModel> vouchers = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                vouchers.addAll(voucherModelsAll);
            }else{
                String pattern = constraint.toString().trim().toLowerCase();
                for(BaseVoucherModel voucherModel :voucherModelsAll){
                    if(voucherModel.getBaseLedger().toLowerCase().contains(pattern) || voucherModel.getVoucherAmount().toLowerCase().contains(pattern)){
                        vouchers.add(voucherModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = vouchers;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            voucherModels.clear();
            voucherModels.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };



    public static class BaseVoucherViewHolder extends RecyclerView.ViewHolder{
         private AppCompatTextView baseName;
         private AppCompatTextView voucherAmount;
         private AppCompatTextView voucherType;
         private AppCompatTextView voucherInfo;

        public BaseVoucherViewHolder(View v){
            super(v);
            baseName = v.findViewById(R.id.baseLedger);
            voucherAmount = v.findViewById(R.id.voucherAmount);
            voucherType = v.findViewById(R.id.voucherType);
            voucherInfo = v.findViewById(R.id.voucherInfo);

        }

    }

}
