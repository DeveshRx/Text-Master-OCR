package devesh.app.ocr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import devesh.app.ocr.database.DatabaseTool;
import devesh.app.ocr.database.ScanFile;
import devesh.app.ocr.databinding.ActivityResultBinding;
import devesh.app.ocr.utils.AppReviewTask;
import devesh.app.ocr.utils.CachePref;
import devesh.app.ocr.utils.InstallSource;

public class ResultActivity extends AppCompatActivity {
    String TAG = "ResultAct:";
    ActivityResultBinding binding;
    DatabaseTool databaseTool;
    AdMobAPI adMobAPI;
    boolean isAdShowed;
    AppReviewTask appReviewTask;
    String text;
    CachePref cachePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adMobAPI = new AdMobAPI(this);
        cachePref = new CachePref(this);
        Intent intent = getIntent();
        isAdShowed = false;

        text = intent.getStringExtra("text");
        boolean isSingleLine = cachePref.getBoolean(getString(R.string.Pref_isMultiline));
        Log.d(TAG, "onCreate: isSingleLine: " + isSingleLine);
        String Add2DB = "yes";
        try {
            Add2DB = intent.getStringExtra("ad2db");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }

        Log.d(TAG, "onCreate: intent Text: " + text);

        if (text.equals(null) || text.equals("") || text.equals(" ")) {
            Add2DB = "no";
            text = "No Text Found in Image !!";
        }

        if (isSingleLine) {
            text = text.replaceAll("\\s+", " ");

        }
        binding.ResultEditText.setText(text);

        if (Add2DB.equals("yes")) {

            databaseTool = new DatabaseTool(this);
            ScanFile scanFile = new ScanFile();
            scanFile.text = text;
            scanFile.time = System.currentTimeMillis();
            databaseTool.Add(scanFile);

        }
        binding.CopyButton.setOnClickListener(view -> {
            CopyText(text);
        });

        // Essential For Galaxy App Store Policy
        if(!InstallSource.isGalaxyStore(this)){
            adMobAPI.LoadInterstitialAd(this);
        }


        adMobAPI.setAdaptiveBanner(binding.AdFrameLayout, this);
        appReviewTask = new AppReviewTask(this, this);
        appReviewTask.requestAppReview();
    }

    @Override
    public void onBackPressed() {
        if (isAdShowed) {
            super.onBackPressed();
        } else {
            super.onBackPressed();

            if(!InstallSource.isGalaxyStore(this)) {
                adMobAPI.ShowInterstitialAd();
            }

            isAdShowed = true;
        }


    }

    void CopyText(String text) {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("OCR", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }


}