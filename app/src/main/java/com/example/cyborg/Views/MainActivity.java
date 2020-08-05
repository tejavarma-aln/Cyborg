package com.example.cyborg.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.cyborg.Interface.NetworkInterface;
import com.example.cyborg.Interface.OnIpValidated;
import com.example.cyborg.Interface.OnUserDataParsed;
import com.example.cyborg.Parsers.UserParser;
import com.example.cyborg.R;
import com.example.cyborg.Utils.Constants;
import com.example.cyborg.Utils.PayLoads;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity implements OnIpValidated {

     private AppCompatEditText tallyIp;
     private AppCompatEditText tallyPort;
     private SharedPreferences sharedPreferences;
     private RequestBody requestBody;
     private AppCompatButton connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectButton = findViewById(R.id.connectButton);
        tallyIp = findViewById(R.id.tallyIpAddress);
        tallyPort = findViewById(R.id.tallyPort);
        requestBody = RequestBody.create(MediaType.parse("text/xml"),new PayLoads().getCompanyPayLoad());
        sharedPreferences = getSharedPreferences("connectionDetails",MODE_PRIVATE);
        tallyIp.setText(sharedPreferences.getString("IPADDRESS",""));
        tallyPort.setText(sharedPreferences.getString("TALLYPORT",""));
        connectButton.setOnClickListener(v -> {
          if(tallyIp.getText() == null || tallyPort.getText() == null){
              showSnackBar("Invalid Details");
          }else{
              connectButton.setEnabled(false);
              validateIp();
          }

        });


    }

    private void validateIp() {
        CheckIp checkip = new CheckIp(this);
        checkip.execute(tallyIp.getText().toString().trim());
    }

    private void proceedToHome(){
        startActivity(new Intent(this, HomeActivity.class));
        this.finish();
    }


    private void updatePreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IPADDRESS",tallyIp.getText().toString());
        editor.putString("TALLYPORT",tallyPort.getText().toString());
        editor.apply();
    }

    private void showSnackBar(String message){
        Snackbar snackbar =  Snackbar.make(findViewById(R.id.activityMain),message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private void parseUserInfo(String data){
        UserParser userParser = new UserParser(new OnUserDataParsed() {
            @Override
            public void OnParsed(String[] data) {
                updatePreferences();
                Constants.COMPANY_NAME = data[0];
                Constants.SERIAL_NUMBER = data[1];
                connectButton.setEnabled(true);
                proceedToHome();
            }

            @Override
            public void OnParseFailed(String err) {
                   showSnackBar(err);
                   connectButton.setEnabled(true);
            }
        });
        userParser.execute(data);
    }



    @Override
    public void OnSuccess() {
        Constants.TALLY_IP = tallyIp.getText().toString().trim();
        Constants.TALLY_PORT = tallyPort.getText().toString().trim();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://".concat(Constants.TALLY_IP).concat(":").concat(Constants.TALLY_PORT)).build();
        showSnackBar("Connecting on :".concat("http://".concat(Constants.TALLY_IP).concat(":").concat(Constants.TALLY_PORT)));
        NetworkInterface networkInterface = retrofit.create(NetworkInterface.class);
        Call<ResponseBody> call = networkInterface.getData(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        parseUserInfo(response.body().string());
                    }

                } catch (IOException e) {
                    showSnackBar(e.getMessage());
                    connectButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackBar(t.getMessage());
                connectButton.setEnabled(true);
            }
        });
    }

    @Override
    public void OnError() {
        showSnackBar("Invalid Ip Address");
        connectButton.setEnabled(true);

    }


    public static class CheckIp extends AsyncTask<String,Void,Boolean>{

        private OnIpValidated onIpValidated;
        public CheckIp(OnIpValidated onIpValidated){
            this.onIpValidated = onIpValidated;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                onIpValidated.OnSuccess();
            }else {
                onIpValidated.OnError();
            }
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                InetAddress inetAddress = InetAddress.getByName(strings[0]);
                return  inetAddress.getHostAddress().equals(strings[0]);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}