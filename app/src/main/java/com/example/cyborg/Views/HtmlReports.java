package com.example.cyborg.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.cyborg.R;
import com.example.cyborg.Utils.DateUtil;
import com.example.cyborg.ViewModels.HtmlViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class HtmlReports extends AppCompatActivity {

    private MaterialDatePicker<?> datePicker;
    private String[] dates = new String[2];
    private AppCompatButton periodButton;
    private WebView webView;
    private ProgressBar progressBar;
    private HtmlViewModel htmlViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_reports);
        if(getIntent().hasExtra("Report Name")) {
            InitToolBar();
            InitDatePicker();
            progressBar = findViewById(R.id.appProgressBar);
            InitWebView(getIntent().getStringExtra("Report Name"));
        }else{
            showSnackBar("Invalid Input");
            this.finish();
        }
    }

    private void hideProgress(){
        if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
    }

    private void showSnackBar(String message){
        Snackbar snackbar =  Snackbar.make(findViewById(R.id.htmlReports),message,Snackbar.LENGTH_LONG);
        snackbar.show();
        hideProgress();
    }

    private void toggleView(){
        if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
        if(webView.getVisibility() == View.GONE) webView.setVisibility(View.VISIBLE);
    }

    private void InitWebView(String ReportName) {
        webView  = findViewById(R.id.webViewContainer);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        htmlViewModel = new ViewModelProvider(this).get(HtmlViewModel.class);
        htmlViewModel.setReportName(ReportName);
        htmlViewModel.getReport().observe(this,data->{
            webView.loadData(data,"text/html","UTF-8");
            toggleView();
        });
        htmlViewModel.getError().observe(this,this::showSnackBar);
        htmlViewModel.pullData();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void  updateView(String[] period) {
        dates[0] = period[0];
        dates[1] = period[1];
        htmlViewModel.updateModel(period[0],period[1]);
        periodButton.setText(String.format("%s  -  %s",dates[0],dates[1]));
    }

    private void getPeriod() {
        if(datePicker != null) {
            datePicker.show(getSupportFragmentManager(), "PICK_PERIOD");
        }
    }


    private void InitToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Balance Sheet");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}