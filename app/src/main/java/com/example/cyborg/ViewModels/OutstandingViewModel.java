package com.example.cyborg.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cyborg.Interface.OnNetworkTaskCompleted;
import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.OutstandingModel;
import com.example.cyborg.Parsers.OutStandingParser;
import com.example.cyborg.Repository.NetworkRepository;
import com.example.cyborg.Utils.PayLoads;

import java.util.ArrayList;

public class OutstandingViewModel extends ViewModel implements OnNetworkTaskCompleted, OnParseCompleted {

    private MutableLiveData<ArrayList<OutstandingModel>>  vouchers;

    private MutableLiveData<String> error;

    private PayLoads payLoads;

    private MutableLiveData<Boolean> hasData;

    private String reportName ;

    private NetworkRepository networkRepository;

    public OutstandingViewModel(){
        this.vouchers = new MutableLiveData<>();
        this.error = new MutableLiveData<>();
        hasData = new MutableLiveData<>();
        this.payLoads = new PayLoads();
        this.networkRepository = new NetworkRepository(this);
        this.reportName = null;
    }

    private void pullVouchers() {
     networkRepository.doTask(payLoads.getOutstandingPayLoad(this.reportName,"0","0"));

    }

    public LiveData<Boolean> hasData(){
        return hasData;
    }

    public void setReport(String reportName){
        this.reportName = reportName;
    }

    public LiveData<String> getError(){
        return error;
    }

    public LiveData<ArrayList<OutstandingModel>> getData(){
        pullVouchers();
        return vouchers;
    }

    public void updateView(String fromDate,String toDate){
        networkRepository.doTask(payLoads.getOutstandingPayLoad(this.reportName,fromDate,toDate));

    }

    @Override
    public void OnSuccess(String res) {
       new OutStandingParser(this).execute(res);
    }

    @Override
    public void OnError(String err) {
           error.setValue(err);
    }

    @Override
    public void OnParsed(ArrayList<?> data) {
     if(data.size() > 0){
         hasData.setValue(true);
         vouchers.setValue((ArrayList<OutstandingModel>) data);
     }else{
         hasData.setValue(false);
     }
    }

    @Override
    public void OnParseFailed(String err) {
         error.setValue(err);
    }
}
