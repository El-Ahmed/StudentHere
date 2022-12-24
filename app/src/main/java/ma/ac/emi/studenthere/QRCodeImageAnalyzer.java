package ma.ac.emi.studenthere;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class QRCodeImageAnalyzer implements ImageAnalysis.Analyzer{
    private final BarcodeScannerOptions options;
    private final QRCodeFoundListener listener;
    private String lastText;

    public QRCodeImageAnalyzer(QRCodeFoundListener listener) {
        options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE)
                        .build();
        this.listener = listener;
        this.lastText = "";

    }
    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        @SuppressLint("UnsafeOptInUsageError") Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            // Pass image to an ML Kit Vision API
            // ...
            BarcodeScanner scanner = BarcodeScanning.getClient(options);
            Task<List<Barcode>> result = scanner.process(image)
                    .addOnCompleteListener(results -> imageProxy.close())
                    .addOnSuccessListener(barcodes -> {
                        // Task completed successfully
                        for (Barcode barcode:
                             barcodes
                             ) {
                            String text = barcode.getRawValue();
                            if(!lastText.equals(text)) {

                                lastText = text;
                                listener.onQRCodeFound(lastText);
                            }


                        }
                    })
                    .addOnFailureListener(e -> {
                        // Task failed with an exception
                        // ...
                    });


        }
    }
}
