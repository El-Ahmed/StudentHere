package ma.ac.emi.studenthere;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ma.ac.emi.studenthere.history.History;
import ma.ac.emi.studenthere.history.JsonPlaceHolderApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttendanceService extends IntentService {


    public AttendanceService() {
        super("AttendanceService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        String qrCode = intent.getStringExtra("qrCode");
        getCourse(qrCode);
    }

    private void getCourse(String qrCode) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ADDRESS_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<History> call = jsonPlaceHolderApi.attend(qrCode, getToken());

        call.enqueue(new Callback<History>() {
             @Override
             public void onResponse(Call<History> call, Response<History> response) {

                 // not connected
                 if (response.code() == 401) {

                     Intent broadcastIntent = new Intent("not-connected");
                     sendLocalBroadcast(broadcastIntent);
                     return;
                 }

                 // wrong qr Code
                 if (!response.isSuccessful()) {



                     Intent broadcastIntent = new Intent("wrong-qr");
                     sendLocalBroadcast(broadcastIntent);
                     return;
                 }
                 // connected and good qr code
                 Intent broadcastIntent = new Intent("attended");
                 sendLocalBroadcast(broadcastIntent);
             }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                Toast.makeText(AttendanceService.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    // get the token
    private String getToken() {
        SharedPreferences sp = getSharedPreferences("LoginFile", Context.MODE_PRIVATE);
        String token=sp.getString("token","notconnected");
        return "Bearer "+token;
    }

}