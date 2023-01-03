package ma.ac.emi.studenthere;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import ma.ac.emi.studenthere.history.History;
import ma.ac.emi.studenthere.history.JsonPlaceHolderApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final int QRCODE_RESULT = 101;
    private TextView attendanceView;
    private ProgressBar loading;
    private Button button;
    private Button btnHistory;
    private Button signoutBtn;

    private final BroadcastReceiver attendedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Attended",Toast.LENGTH_LONG).show();
            stopLoading();
            getLastHistory();
        }
    };
    private final BroadcastReceiver notConnectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Login Expired",Toast.LENGTH_LONG).show();
            stopLoading();
            login();
        }
    };
    private final BroadcastReceiver wrongQrReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Wrong QR code", Toast.LENGTH_LONG).show();
            stopLoading();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if connected
        SharedPreferences sp = getSharedPreferences("LoginFile", Context.MODE_PRIVATE);
        String token=sp.getString("token","notconnected");
        Intent intent0 = new Intent(this, LoginActivity.class);

        // make sure onCreate wasn't called twice
        if (savedInstanceState == null) {
            if(token.equals("notconnected")){
                Log.d("login0", "onCreate: ");
                startActivity(intent0);
            }
        }


        button = findViewById(R.id.button);
        attendanceView = findViewById(R.id.attendance);
        loading = findViewById(R.id.progressBar);

        Intent intent = new Intent(this, QRScanner.class);
        button.setOnClickListener(view ->
                startActivityForResult(intent, QRCODE_RESULT ));

        btnHistory = findViewById(R.id.btnHistory);
        Intent intent1 = new Intent(this, HistoryActivity.class);
        btnHistory.setOnClickListener(view -> startActivity(intent1));

        //logout
        signoutBtn=findViewById(R.id.signoutBtn);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("LoginFile",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token","notconnected");
                editor.commit();
                login();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(attendedReceiver,
                new IntentFilter("attended"));
        LocalBroadcastManager.getInstance(this).registerReceiver(notConnectedReceiver,
                new IntentFilter("not-connected"));
        LocalBroadcastManager.getInstance(this).registerReceiver(wrongQrReceiver,
                new IntentFilter("wrong-qr"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!getToken().equals("Bearer notconnected")) {
            getLastHistory();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                showLoading();

                String qrCode = data.getData().toString();
                Intent attendingIntent = new Intent(this, AttendanceService.class);
                attendingIntent.putExtra("qrCode", qrCode);


                startService(attendingIntent);
            }
            else {
                Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
        }

    }

    private void stopLoading() {

        loading.setVisibility(View.GONE);
        attendanceView.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
    }


    // show a loading progress bar
    // and hide everything else
    private void showLoading() {
        attendanceView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    private void getLastHistory() {
        attendanceView.setText(getString(R.string.attendance));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ADDRESS_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<History> call = jsonPlaceHolderApi.getLastHistory(getToken());

        call.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                if (response.code() == 401) {
                    login();
                    return;
                }
                if(!response.isSuccessful()){
//                    Toast.makeText(HistoryActivity.this, "Code: " + response.code() ,Toast.LENGTH_LONG).show();
                    return;
                }
                History history = response.body();

                attendanceView.setText("Last attend: \n"+history.getCourseName()+" / "+history.getDate().toLocaleString());

            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {

            }
        });
    }

    // get the token
    private String getToken() {
        SharedPreferences sp = getSharedPreferences("LoginFile", Context.MODE_PRIVATE);
        String token=sp.getString("token","notconnected");
        return "Bearer "+token;
    }


    private void login() {
        Log.d("login0", "func: ");
        Intent intent0 = new Intent(this, LoginActivity.class);
        startActivity(intent0);

    }


}