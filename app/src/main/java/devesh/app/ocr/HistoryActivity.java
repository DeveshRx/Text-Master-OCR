package devesh.app.ocr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import devesh.app.ocr.adapter.HistoryAdapter;
import devesh.app.ocr.database.DatabaseTool;

import devesh.app.ocr.database.ScanFile;
import devesh.app.ocr.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {
    String TAG = "HistoryAct";
    ActivityHistoryBinding binding;
    DatabaseTool databaseTool;
    List<ScanFile> scanFileList = new ArrayList<>();
    devesh.app.ocr.AdMobAPI adMobAPI;
    boolean isAdShowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adMobAPI = new AdMobAPI(this);
        databaseTool = new DatabaseTool(this);
        isAdShowed = false;
        adMobAPI.LoadInterstitialAd(this);
        adMobAPI.setAdaptiveBanner(binding.AdFrameLayout, this);
    }

    @Override
    public void onBackPressed() {
        if (isAdShowed) {
            super.onBackPressed();
        } else {
            super.onBackPressed();

            adMobAPI.ShowInterstitialAd();

            isAdShowed = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanFileList = databaseTool.getAll();
        loadRecycleView();
        binding.HistoryButton.setOnClickListener(view -> {
            databaseTool.clearHistory();
            loadRecycleView();
            Toast.makeText(this, "History Deleted", Toast.LENGTH_SHORT).show();
            HistoryActivity.this.finish();
        });
    }

    void loadRecycleView() {

        HistoryAdapter mAdapter = new HistoryAdapter(this, scanFileList);
        binding.HistoryRecycleView.removeAllViews();
        binding.HistoryRecycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.HistoryRecycleView.setAdapter(mAdapter);

    }

    public void OpenHistoryFile(int i) {
        Log.d(TAG, "OpenHistoryFile: ");

        Intent resultActivity = new Intent(this, ResultActivity.class);
        resultActivity.putExtra("text", scanFileList.get(i).text);
        resultActivity.putExtra("ad2db", "no");
        startActivity(resultActivity);

    }

    public void CopyText(int i) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("OCR", scanFileList.get(i).text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }

    public void ShareText(int i) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, scanFileList.get(i).text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

}