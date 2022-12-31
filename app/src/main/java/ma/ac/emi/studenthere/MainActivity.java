package ma.ac.emi.studenthere;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    private ProgressBar loading;
    private Button button;
    private Button btnHistory;

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            stopLoading();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        attendanceView = findViewById(R.id.attendance);
        loading = findViewById(R.id.progressBar);

        Intent intent = new Intent(this, QRScanner.class);
        button.setOnClickListener(view ->
                startActivityForResult(intent, QRCODE_RESULT ));

        btnHistory = findViewById(R.id.btnHistory);
        Intent intent1 = new Intent(this, HistoryActivity.class);
        btnHistory.setOnClickListener(view -> startActivity(intent1));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("response-attendance"));
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

}