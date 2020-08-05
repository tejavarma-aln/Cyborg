package com.example.cyborg.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Models.OutstandingModel;
import com.example.cyborg.R;

import java.util.ArrayList;

public class OutStandingAdapter extends RecyclerView.Adapter<OutStandingAdapter.OutStandingViewHolder> implements Filterable {

    private ArrayList<OutstandingModel> outstandingModels;
    private ArrayList<OutstandingModel> outstandingModelsAll;

    public OutStandingAdapter(ArrayList<OutstandingModel> models){
        this.outstandingModels = models;
        this.outstandingModelsAll = new ArrayList<>(models);
    }

    public void updateAdapter(ArrayList<OutstandingModel> data){
        outstandingModelsAll.clear();
        outstandingModels.clear();
        outstandingModelsAll.addAll(data);
        outstandingModels.addAll(data);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OutStandingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outstanding_view_holder,parent,false);
        return new OutStandingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OutStandingViewHolder holder, int position) {

        OutstandingModel currentModel = outstandingModels.get(position);
        holder.outstandingLedger.setText(currentModel.getLedgerName());
        holder.outstandingAmount.setText(currentModel.getBalanceAmount());
        holder.outstandingInfo.setText(String.format("%s | %s",currentModel.getVoucherDate(),currentModel.getVoucherRef()));
        holder.outstandingDueOn.setText(String.format("Due On : %s",currentModel.getBalanceDueOn()));
        holder.outstandingOverDue.setText(String.format("Over Due : %s",currentModel.getBalanceOverDue()));

    }

    @Override
    public int getItemCount() {
        return outstandingModels.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<OutstandingModel> temp = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                 temp.addAll(outstandingModelsAll);
            }else{
                String pattern = constraint.toString().trim().toLowerCase();
                for(OutstandingModel outstandingModel : outstandingModelsAll){
                    if(outstandingModel.getLedgerName().toLowerCase().contains(pattern)){
                         temp.add(outstandingModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = temp;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            outstandingModels.clear();
            outstandingModels.addAll((ArrayList) results.values);
            notifyDataSetChanged();

        }
    };

    public static class OutStandingViewHolder extends RecyclerView.ViewHolder{

        private AppCompatTextView outstandingLedger;
        private AppCompatTextView outstandingAmount;
        private AppCompatTextView outstandingInfo;
        private AppCompatTextView outstandingDueOn;
        private AppCompatTextView outstandingOverDue;


        public  OutStandingViewHolder(View v){
            super(v);

            outstandingLedger = v.findViewById(R.id.outstandingLedger);
            outstandingAmount = v.findViewById(R.id.outstandingAmount);
            outstandingInfo = v.findViewById(R.id.outstandingInfo);
            outstandingDueOn = v.findViewById(R.id.outstandingDueOn);
            outstandingOverDue = v.findViewById(R.id.outstandingOverDue);
        }

    }
}
