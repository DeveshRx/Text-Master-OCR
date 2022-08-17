package devesh.app.user_guide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import devesh.app.user_guide.databinding.ActivityUserGuideBinding;

public class UserGuideActivity extends AppCompatActivity {
ActivityUserGuideBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}