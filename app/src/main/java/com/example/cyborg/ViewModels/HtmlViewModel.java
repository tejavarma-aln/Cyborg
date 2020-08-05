package com.example.cyborg.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cyborg.Interface.OnNetworkTaskCompleted;
import com.example.cyborg.Repository.NetworkRepository;
import com.example.cyborg.Utils.PayLoads;

public class HtmlViewModel extends ViewModel implements OnNetworkTaskCompleted {

    private MutableLiveData<String> htmlData;

    private MutableLiveData<String> error;

    private String reportName;

    private PayLoads payLoads;

    private NetworkRepository networkRepository;

    public HtmlViewModel(){
        this.htmlData = new MutableLiveData<>();
        this.error = new MutableLiveData<>();
        this.reportName = null;
        this.payLoads = new PayLoads();
        this.networkRepository = new NetworkRepository(this);
    }

    public void setReportName(String reportName){
        this.reportName = reportName;
    }

    public LiveData<String> getError(){
        return error;
    }

    public void pullData() {
     networkRepository.doTask(payLoads.getHtmlPayload(reportName,"0","0"));

    }

    public void updateModel(String fromDate,String toDate){
        networkRepository.doTask(payLoads.getHtmlPayload(reportName,fromDate,toDate));

    }

    public LiveData<String> getReport(){
        pullData();
        return  htmlData;
    }

    @Override
    public void OnSuccess(String res) {
        htmlData.setValue(res.replace("#", "%23"));
    }

    @Override
    public void OnError(String err) {
         error.setValue(err);
    }
}
