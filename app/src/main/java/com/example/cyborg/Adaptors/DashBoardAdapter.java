package com.example.cyborg.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.cyborg.Models.DashBoardCardModel;
import com.example.cyborg.R;
import com.example.cyborg.Interface.OnDashBoardClick;

import java.util.ArrayList;

public class DashBoardAdapter extends BaseAdapter {

    private ArrayList<DashBoardCardModel> dashBoardCardModels;
    private OnDashBoardClick dashBoardClick;

    public DashBoardAdapter(ArrayList<DashBoardCardModel> dashBoardCardModels,OnDashBoardClick dashBoardClick) {
        this.dashBoardCardModels = dashBoardCardModels;
        this.dashBoardClick = dashBoardClick;
    }

    private static  class  DashBoardViewHolder implements View.OnClickListener {
        private AppCompatTextView name;
        private AppCompatImageView image;
        private OnDashBoardClick onDashBoardClick;
        private int position;

        public DashBoardViewHolder(View v, OnDashBoardClick listener,int position){
            name = v.findViewById(R.id.cardText);
            image = v.findViewById(R.id.cardImage);
            this.position = position;

            this.onDashBoardClick = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDashBoardClick.OnClick(position);

        }
    }


    @Override
    public int getCount() {
        return dashBoardCardModels.size();
    }

    @Override
    public Object getItem(int position) {
        return dashBoardCardModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_cards,parent,false);

        }

        DashBoardViewHolder viewHolder = new DashBoardViewHolder(convertView,dashBoardClick,position);
        DashBoardCardModel cardModel = (DashBoardCardModel) getItem(position);
        viewHolder.image.setImageResource(cardModel.getImageId());
        viewHolder.name.setText(cardModel.getName());

        return convertView;
    }

}
