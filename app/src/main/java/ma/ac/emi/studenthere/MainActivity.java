package ma.ac.emi.studenthere;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private final int QRCODE_RESULT = 101;
    private TextView attendanceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        attendanceView = findViewById(R.id.attendance);

        Intent intent = new Intent(this, QRScanner.class);
        button.setOnClickListener(view ->
                startActivityForResult(intent, QRCODE_RESULT ));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                String qrCode = data.getData().toString();

                getCourse();
            }
            else {
                Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
        }

    }

    private void getCourse() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://c3b6072f-1301-40fe-b039-f0ed1358da3b.mock.pstmn.io/course";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        attendanceView.setText("Attending " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                attendanceView.setText("Failed. Try again");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}