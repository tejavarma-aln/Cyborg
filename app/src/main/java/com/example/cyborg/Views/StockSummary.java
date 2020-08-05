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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Adaptors.StockAdapter;
import com.example.cyborg.R;
import com.example.cyborg.Utils.DateUtil;
import com.example.cyborg.ViewModels.StockViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.zip.GZIPOutputStream;

public class StockSummary extends AppCompatActivity {

     private RecyclerView recyclerView;
     private StockAdapter stockAdapter;
     private MaterialDatePicker<?> datePicker;
     private String[] dates = new String[2];
     private AppCompatButton periodButton;
     private ProgressBar progressBar;
     private LinearLayoutCompat noData;
     private StockViewModel stockViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_summary);
        InitToolBar();
        InitRecycleView();
        InitDatePicker();
        progressBar = findViewById(R.id.appProgressBar);
        noData = findViewById(R.id.appNoData);
        stockViewModel = new ViewModelProvider(this).get(StockViewModel.class);
        stockViewModel.getData().observe(this,data ->{
           if(stockAdapter == null){
               stockAdapter = new StockAdapter(data);
               recyclerView.setAdapter(stockAdapter);
               toggleView();
           }else{
               stockAdapter.updateAdapter(data);
           }
        });
        stockViewModel.hasData().observe(this,this::OnDataEmpty);
        stockViewModel.getError().observe(this, this::showSnackBar);

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

    private void updateAdapter(String[] period) {

        dates[0] = period[0];
        dates[1] = period[1];
        stockViewModel.updateModel(period[0],period[1]);
        periodButton.setText(String.format("%s  -  %s",dates[0],dates[1]));

    }

    private void getPeriod() {

        if(datePicker != null) {
            datePicker.show(getSupportFragmentManager(), "PICK_PERIOD");
        }
    }

    private void InitRecycleView() {
        recyclerView = findViewById(R.id.listOfStocks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showSnackBar(String message){
        Snackbar snackbar =  Snackbar.make(findViewById(R.id.stockSummary),message,Snackbar.LENGTH_LONG);
        snackbar.show();
        hideProgress();
    }


    private void InitToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Stock Summary");
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
                if(stockAdapter != null) stockAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }
}