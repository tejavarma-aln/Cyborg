package com.example.cyborg.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Adaptors.LedgerAdaptor;
import com.example.cyborg.Models.LedgerModel;
import com.example.cyborg.R;
import com.example.cyborg.Interface.OnledgerClickInterface;
import com.example.cyborg.ViewModels.LedgerViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class LedgerActivity extends AppCompatActivity implements OnledgerClickInterface {

    private RecyclerView recyclerView;
    private LedgerAdaptor adapter;
    private ProgressBar progressBar;
    private LinearLayoutCompat noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);
        InitToolBar();
        InitRecycleView();
        progressBar = findViewById(R.id.appProgressBar);
        noData = findViewById(R.id.appNoData);
        LedgerViewModel ledgerViewModal = new ViewModelProvider(this).get(LedgerViewModel.class);
        ledgerViewModal.getLedgers().observe(this, this::UpdateAdapter);
        ledgerViewModal.hasData().observe(this,this::OnDataEmpty);
        ledgerViewModal.getError().observe(this, this::showSnackBar);

    }

    private void showSnackBar(String message){
        Snackbar snackbar =  Snackbar.make(findViewById(R.id.ledgerActivity),message,Snackbar.LENGTH_LONG);
        snackbar.show();
        hideProgress();
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

    private void UpdateAdapter(ArrayList<LedgerModel> ledger) {
        adapter = new LedgerAdaptor(ledger, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        toggleView();
    }

    private void InitToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select a Ledger");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                if(adapter != null) adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void InitRecycleView() {
        recyclerView = findViewById(R.id.ledgerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void OnClick(String ledgerName) {
        Intent ledgerSummary  = new Intent(this, Vouchers.class);
        ledgerSummary.putExtra("Report Name",ledgerName);
        ledgerSummary.putExtra("Type","Ledger");
        startActivity(ledgerSummary);
    }
}












