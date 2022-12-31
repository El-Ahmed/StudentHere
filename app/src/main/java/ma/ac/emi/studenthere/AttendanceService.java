package ma.ac.emi.studenthere;

import android.app.IntentService;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://c3b6072f-1301-40fe-b039-f0ed1358da3b.mock.pstmn.io/course";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    Intent broadcastIntent = new Intent("response-attendance");
                    broadcastIntent.putExtra("message", response);
                    sendLocalBroadcast(broadcastIntent);
                }, error -> {
                    Intent broadcastIntent = new Intent("response-attendance");
                    broadcastIntent.putExtra("error", error);
                    sendLocalBroadcast(broadcastIntent);
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    private void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}