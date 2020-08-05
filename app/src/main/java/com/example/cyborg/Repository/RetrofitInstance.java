package com.example.cyborg.Repository;

import com.example.cyborg.Utils.Constants;

import retrofit2.Retrofit;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static String BASE_URL = "http://".concat(Constants.TALLY_IP).concat(":").concat(Constants.TALLY_PORT);

    public  static  Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        }
        return retrofit;
    }

}
