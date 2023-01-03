package ma.ac.emi.studenthere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ma.ac.emi.studenthere.history.History;
import ma.ac.emi.studenthere.history.HistoryAdapter;
import ma.ac.emi.studenthere.history.JsonPlaceHolderApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        loadHistoryList();

    }

    private void loadHistoryList(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ADDRESS_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<History>> call = jsonPlaceHolderApi.getHistories(getToken());

        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                if(response.code()==401) {

                    Toast.makeText(HistoryActivity.this, "Login Expired", Toast.LENGTH_SHORT).show();
                    login();
                    return;
                }
                if(!response.isSuccessful()){
                    Toast.makeText(HistoryActivity.this, "Code: " + response.code() ,Toast.LENGTH_LONG).show();
                    return;
                }
                List<History> histories = response.body();
                populateListView((ArrayList<History>) histories);
            }
            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Failed to leaod histories" ,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateListView(ArrayList<History> histories){
        // Create the adapter to convert the array to views
        HistoryAdapter adapter = new HistoryAdapter(this, histories);
        ListView listView = (ListView) findViewById(R.id.list);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);
    }

    // get the token
    private String getToken() {
        SharedPreferences sp = getSharedPreferences("LoginFile", Context.MODE_PRIVATE);
        String token=sp.getString("token","notconnected");
        return "Bearer "+token;
    }
    private void login() {
        Intent intent0 = new Intent(this, LoginActivity.class);
        startActivity(intent0);
    }
}