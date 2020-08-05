package com.example.cyborg.Repository;

import com.example.cyborg.Interface.NetworkInterface;
import com.example.cyborg.Interface.OnNetworkTaskCompleted;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkRepository {

    private OnNetworkTaskCompleted taskCompleted;
    private NetworkInterface networkInterface;

    public NetworkRepository(OnNetworkTaskCompleted taskCompleted){
        this.taskCompleted = taskCompleted;
        this.networkInterface =  RetrofitInstance.getRetrofit().create(NetworkInterface.class);
    }

    public void doTask(String payLoad){
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml"), payLoad);
        Call<ResponseBody> call = networkInterface.getData(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String data = null;
                    if (response.body() != null) {
                        data = response.body().string();
                    }
                    taskCompleted.OnSuccess(data);
                } catch (IOException e) {
                    taskCompleted.OnError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                taskCompleted.OnError(t.getMessage());
            }
        });
    }

}
