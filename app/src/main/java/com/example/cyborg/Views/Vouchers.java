package com.example.cyborg.Views;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Adaptors.BaseVoucherAdapter;
import com.example.cyborg.Models.BaseVoucherModel;
import com.example.cyborg.R;
import com.example.cyborg.Utils.Constants;
import com.example.cyborg.Utils.DateUtil;
import com.example.cyborg.ViewModels.BaseVoucherViewModel;
import com.example.cyborg.ViewModels.LedgerVchViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class Vouchers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BaseVoucherAdapter baseVoucherAdapter;
    private MaterialDatePicker<?> datePicker;
    private String[] dates = new String[2];
    private AppCompatButton periodButton;
    private ProgressBar progressBar;
    private LinearLayoutCompat noData;
    private BaseVoucherViewModel baseVoucherViewModel;
    private LedgerVchViewModel ledgerVchViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);
        if((getIntent().hasExtra("Report Name")) && (getIntent().hasExtra("Type"))){
            InitToolBar(getIntent().getStringExtra("Report Name"));
            InitRecycleView();
            InitDatePicker();
            progressBar = findViewById(R.id.appProgressBar);
            noData = findViewById(R.id.appNoData);
            handleDataModel(getIntent().getStringExtra("Report Name"),getIntent().getStringExtra("Type"));
        }else {
            showSnackBar("Invalid Input");
            this.finish();
        }



    }

    private void handleDataModel(String report_name, String type) {
        if(type.equalsIgnoreCase("Ledger")){
            ledgerVchViewModel = new ViewModelProvider(this).get(LedgerVchViewModel.class);
            ledgerVchViewModel.setReportName(report_name);
            ledgerVchViewModel.getData().observe(this,this::InitView);
            ledgerVchViewModel.getError().observe(this,this::showSnackBar);
            ledgerVchViewModel.hasData().observe(this,this::OnDataEmpty);
        }
        if(type.equalsIgnoreCase("Report")){
            baseVoucherViewModel = new ViewModelProvider(this).get(BaseVoucherViewModel.class);
            baseVoucherViewModel.setReport(report_name);
            baseVoucherViewModel.getVoucher().observe(this,this::InitView);
            baseVoucherViewModel.getError().observe(this,this::showSnackBar);
            baseVoucherViewModel.hasData().observe(this,this::OnDataEmpty);
        }

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

    private void showSnackBar(String message){
        Snackbar snackbar =  Snackbar.make(findViewById(R.id.activityVouchers),message,Snackbar.LENGTH_LONG);
        snackbar.show();
        hideProgress();
    }


    private void InitView(ArrayList<BaseVoucherModel> models){
        if(baseVoucherAdapter == null){
            baseVoucherAdapter = new BaseVoucherAdapter(models);
            recyclerView.setAdapter(baseVoucherAdapter);
            toggleView();
        }else {
            baseVoucherAdapter.updateAdapter(models);
        }
    }

    private void hideProgress(){
        if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
    }

    private void toggleView(){
        if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
        if(recyclerView.getVisibility() == View.GONE) recyclerView.setVisibility(View.VISIBLE);
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
                updateAdapter(period);
            }else if(!dates[0].equals(period[0]) || !dates[1].equals(period[1])){
                updateAdapter(period);
            }

        });
    }

    private void updateAdapter(String[] period) {
        dates[0] = period[0];
        dates[1] = period[1];
        if(baseVoucherViewModel != null) baseVoucherViewModel.updateModel(period[0],period[1]);
        if(ledgerVchViewModel != null) ledgerVchViewModel.updateModel(period[0],period[1]);
        periodButton.setText(String.format("%s  -  %s",dates[0],dates[1]));
    }

    private void getPeriod() {
        if(datePicker != null) {
            datePicker.show(getSupportFragmentManager(), "PICK_PERIOD");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.search_menu,menu);
         MenuItem searchItem = menu.findItem(R.id.appSearchBar);
         SearchView searchView = (SearchView) searchItem.getActionView();
         ImageView searchClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
         searchClose.setColorFilter(Color.WHITE);
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String newText) {
                if(baseVoucherAdapter != null) baseVoucherAdapter.getFilter().filter(newText);
                 return true;
             }
         });
         return true;
    }

    private void InitRecycleView() {
        recyclerView = findViewById(R.id.listOfVouchers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void InitToolBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}