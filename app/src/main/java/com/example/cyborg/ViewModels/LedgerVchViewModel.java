package com.example.cyborg.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cyborg.Interface.OnNetworkTaskCompleted;
import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.BaseVoucherModel;
import com.example.cyborg.Parsers.LedgerVchParser;
import com.example.cyborg.Repository.NetworkRepository;
import com.example.cyborg.Utils.PayLoads;

import java.util.ArrayList;

public class LedgerVchViewModel extends ViewModel implements OnNetworkTaskCompleted, OnParseCompleted {

    private MutableLiveData<ArrayList<BaseVoucherModel>> ledgerVouchers;

    private MutableLiveData<String> error;

    private NetworkRepository networkRepository;

    private PayLoads payLoads;

    private String reportName;

    private MutableLiveData<Boolean> hasData;


    public LedgerVchViewModel(){
        this.ledgerVouchers = new MutableLiveData<>();
        this.error = new MutableLiveData<>();
        hasData = new MutableLiveData<>();
        this.payLoads = new PayLoads();
        this.networkRepository = new NetworkRepository(this);
        this.reportName = null;

    }

    public LiveData<ArrayList<BaseVoucherModel>> getData(){
        pullData();
        return ledgerVouchers;
    }

    public LiveData<Boolean> hasData(){
        return hasData;
    }

    public void setReportName(String reportName){
        this.reportName = reportName;
    }

    private void pullData() {
        networkRepository.doTask(payLoads.getLedVchPayLoad(this.reportName,"0","0"));
    }

    public void updateModel(String fromDate,String toDate){
        networkRepository.doTask(payLoads.getLedVchPayLoad(this.reportName,fromDate,toDate));

    }

    public LiveData<String> getError(){
        return error;
    }

    @Override
    public void OnSuccess(String res) {
       new LedgerVchParser(this).execute(res);
    }

    @Override
    public void OnError(String err) {
     error.setValue(err);
    }

    @Override
    public void OnParsed(ArrayList<?> data) {
        if(data.size() > 0){
            hasData.setValue(true);
            ledgerVouchers.setValue((ArrayList<BaseVoucherModel>) data);
        }else{
            hasData.setValue(false);
        }
    }


    @Override
    public void OnParseFailed(String err) {
       error.setValue(err);
    }
}
