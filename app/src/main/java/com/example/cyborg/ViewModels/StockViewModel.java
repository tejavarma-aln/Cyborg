package com.example.cyborg.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cyborg.Interface.OnNetworkTaskCompleted;
import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.StockModel;
import com.example.cyborg.Parsers.StockParser;
import com.example.cyborg.Repository.NetworkRepository;
import com.example.cyborg.Utils.PayLoads;

import java.util.ArrayList;

public class StockViewModel extends ViewModel implements OnNetworkTaskCompleted, OnParseCompleted {

    private MutableLiveData<ArrayList<StockModel>> stocks;

    private MutableLiveData<String> error;

    private PayLoads payLoads;

    private MutableLiveData<Boolean> hasData;

    private NetworkRepository networkRepository;

    public StockViewModel(){
        stocks = new MutableLiveData<>();
        error = new MutableLiveData<>();
        payLoads = new PayLoads();
        hasData = new MutableLiveData<>();
        networkRepository = new NetworkRepository(this);

    }

    public LiveData<Boolean> hasData(){
        return hasData;
    }

    private void pullData() {
       networkRepository.doTask(payLoads.getStockPayLoad("0","0"));
    }

    public LiveData<String> getError(){
        return error;
    }

    public LiveData<ArrayList<StockModel>> getData(){
        pullData();
        return  stocks;
    }

    public void updateModel(String fromDate,String toDate){
        networkRepository.doTask(payLoads.getStockPayLoad(fromDate,toDate));
    }

    @Override
    public void OnSuccess(String res) {
      new StockParser(this).execute(res);
    }

    @Override
    public void OnError(String err) {
         error.setValue(err);
    }


    @Override
    public void OnParsed(ArrayList<?> data){
        if(data.size() > 0){
            hasData.setValue(true);
            stocks.setValue((ArrayList<StockModel>) data);
        }else{
            hasData.setValue(false);
        }
    }

    @Override
    public void OnParseFailed(String err) {
      error.setValue(err);
    }
}
