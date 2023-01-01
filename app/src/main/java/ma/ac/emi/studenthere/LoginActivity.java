package ma.ac.emi.studenthere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ma.ac.emi.studenthere.login.LoginRequest;
import ma.ac.emi.studenthere.login.LoginResponse;
import ma.ac.emi.studenthere.login.RetrofitClient;
import ma.ac.emi.studenthere.login.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText usernameField;
    EditText passwordField;
    Button loginButton;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        //api url
        userService= RetrofitClient.getClient("http://192.168.1.4:8080/").create(UserService.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=usernameField.getText().toString();
                String password=passwordField.getText().toString();
                if(validateLogin(username, password)){
                    doLogin(username,password);
                }
            }
        });
    }
    //check if field are full
    private boolean validateLogin(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //login function
    private void doLogin(final String username,final String password){
        Call<LoginResponse> call = userService.login(new LoginRequest(username, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    LoginResponse resObj = response.body();
                    String token=resObj.getToken();
                    Toast.makeText(LoginActivity.this, "connected", Toast.LENGTH_SHORT).show();
                    //Store token in local storage
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginFile",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);

                    //Switch to mainActivity after token is stored
                    if(editor.commit()){
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "The username or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}