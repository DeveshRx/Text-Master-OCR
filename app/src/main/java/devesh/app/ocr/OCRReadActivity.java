package devesh.app.ocr;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;

import devesh.app.ocr.databinding.ActivityOcrreadBinding;

public class OCRReadActivity extends AppCompatActivity {

    ActivityOcrreadBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mBinding = ActivityOcrreadBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


    }

    @Override
    protected void onStart() {
        super.onStart();

        File file = new File(getFilesDir(), "img_cache.png");

    }

    void initTextRec() {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);


    }
}