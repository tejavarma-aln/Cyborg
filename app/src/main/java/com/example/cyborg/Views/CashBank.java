package com.example.cyborg.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Adaptors.CashBankAdaptor;
import com.example.cyborg.R;
import com.example.cyborg.Utils.DateUtil;
import com.example.cyborg.ViewModels.CashBankViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class CashBank extends AppCompatActivity {

    private MaterialDatePicker<?> datePicker;
    private String[] dates = new String[2];
    private AppCompatButton periodButton;
    private RecyclerView recyclerView;
    private CashBankAdaptor cashBankAdaptor;
    private ProgressBar progressBar;
    private LinearLayoutCompat noData;
    private CashBankViewModel cashBankViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashand_bank);
        InitToolBar();
        InitDatePicker();
        InitRecycleView();
        progressBar = findViewById(R.id.appProgressBar);
        noData = findViewById(R.id.appNoData);
        cashBankViewModel = new ViewModelProvider(this).get(CashBankViewModel.class);
        cashBankViewModel.getData().observe(this,data->{
            if(cashBankAdaptor == null){
                cashBankAdaptor = new CashBankAdaptor(data);
                recyclerView.setAdapter(cashBankAdaptor);
                toggleView();
            }else{
                cashBankAdaptor.updateAdapter(data);
            }


        });
        cashBankViewModel.hasData().observe(this,this::OnDataEmpty);
        cashBankViewModel.getError().observe(this,this::showSnackBar);
    }


    private void hideProgress(){
        if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
    }

    private void OnDataEmpty(boolean hasData){
        if(hasData){
            if(recyclerView.getVisibility() == View.GONE)recyclerView.setVisibility(View.VISIBLE);
            if(noData.getVisibility() == View.VISIBLE) noData.setVisibility(View.GONE);
        }else{
            if(recyclerView.getVisibility() == View.VISIBLE)recyclerView.setVisibility(View.GONE);
            if(noData.getVisibility() == View.GONE) noData.setVisibility(View.VISIBLE);
            if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);

        }
    }

    private void toggleView(){
        if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
        if(recyclerView.getVisibility() == View.GONE) recyclerView.setVisibility(View.VISIBLE);
    }

    private void showSnackBar(String message){
        Snackbar snackbar =  Snackbar.make(findViewById(R.id.cashAndBank),message,Snackbar.LENGTH_LONG);
        snackbar.show();
        hideProgress();
    }

    private void InitRecycleView() {
        recyclerView = findViewById(R.id.CashBankData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void InitToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cash and Bank");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void InitDatePicker() {
        periodButton = findViewById(R.id.period_button);
        periodButton.setOnClickListener(v -> getPeriod());
        MaterialDatePicker.Builder<Pair<Long,Long>> dateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        dateBuilder.setTitleText("Select Period");
        datePicker = dateBuilder.build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            Pair<Long,Long> selectedPeriod = (Pair) selection;
            String[] period = DateUtil.resolvePeriod(selectedPeriod);
            if(dates[0] == null || dates[1] == null){
                updateView(period);
            }else if(!dates[0].equals(period[0]) || !dates[1].equals(period[1])){
                updateView(period);
            }

        });
    }

    private void updateView(String[] period) {
        dates[0] = period[0];
        dates[1] = period[1];
        cashBankViewModel.updateView(period[0],period[1]);
        periodButton.setText(String.format("%s  -  %s",dates[0],dates[1]));
    }

    private void getPeriod() {
        if(datePicker != null) {
            datePicker.show(getSupportFragmentManager(), "PICK_PERIOD");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}