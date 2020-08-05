package com.example.cyborg.ViewModels;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cyborg.Interface.OnNetworkTaskCompleted;
import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.BaseVoucherModel;
import com.example.cyborg.Parsers.BaseVchParser;
import com.example.cyborg.Repository.NetworkRepository;
import com.example.cyborg.Utils.PayLoads;

import java.util.ArrayList;

public class BaseVoucherViewModel extends ViewModel implements OnNetworkTaskCompleted, OnParseCompleted {

    private MutableLiveData<ArrayList<BaseVoucherModel>> voucherModels;
    private MutableLiveData<String> error;
    private PayLoads payLoads;
    private NetworkRepository networkRepository;
    private String reportName;
    private MutableLiveData<Boolean> hasData;

    public BaseVoucherViewModel(){
        this.voucherModels = new MutableLiveData<>();
        this.error = new MutableLiveData<>();
        hasData = new MutableLiveData<>();
        this.payLoads = new PayLoads();
        this.networkRepository = new NetworkRepository(this);
        this.reportName = null;


    }

    public LiveData<Boolean> hasData(){
        return hasData;
    }

    public void setReport(String reportName){
        this.reportName = reportName;
    }

    public LiveData<String> getError(){
        return  error;
    }


    public LiveData<ArrayList<BaseVoucherModel>> getVoucher(){
        pullVoucher();
        return voucherModels;
    }


    private void pullVoucher(){
        networkRepository.doTask(payLoads.getVouchers("0","0",this.reportName));
    }

    public void updateModel(String fromDate,String toDate){
        networkRepository.doTask(payLoads.getVouchers(fromDate,toDate,this.reportName));

    }

    @Override
    public void OnSuccess(String res) {
        new BaseVchParser(this).execute(res);
    }

    @Override
    public void OnError(String err) {
         error.setValue(err);
    }

    @Override
    public void OnParsed(ArrayList<?> data) {
      if(data.size() > 0) {
          hasData.setValue(true);
          voucherModels.setValue((ArrayList<BaseVoucherModel>) data);
      }else {
          hasData.setValue(false);
      }
    }


    @Override
    public void OnParseFailed(String err) {
       error.setValue(err);
    }

}
