package devesh.app.ocr.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import devesh.app.ocr.databinding.FragmentResultBinding;


public class ResultFragment extends Fragment {
    String TAG = "ResultFrag: ";
    FragmentResultBinding binding;
    String ResultText;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        try {
            ResultText = getArguments().getString("text");
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }

        Log.d(TAG, "onCreateView: TEXT: " + ResultText);
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        //binding.editTextScanResult.setText(ResultText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}