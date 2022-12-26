package ma.ac.emi.studenthere;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int QRCODE_RESULT = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, QRScanner.class);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view ->
                startActivityForResult(intent, QRCODE_RESULT ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                String qrCode = data.getData().toString();
                Toast.makeText(this,qrCode,Toast.LENGTH_SHORT).show();
            }


        }

    }
}