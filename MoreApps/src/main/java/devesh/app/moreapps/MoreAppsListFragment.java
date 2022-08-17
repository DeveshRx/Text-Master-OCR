package devesh.app.moreapps;

import android.content.ClipData;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import devesh.app.moreapps.databinding.FragmentMoreAppsBinding;


public class MoreAppsListFragment extends Fragment {

Gson gson;
FragmentMoreAppsBinding binding;
ItemsAdapter itemsAdapter;
public MoreAppsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
     //       mParam1 = getArguments().getString(ARG_PARAM1);
       //     mParam2 = getArguments().getString(ARG_PARAM2);
        }
        gson=new Gson();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMoreAppsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }



    @Override
    public void onStart() {
        super.onStart();
        InputStream fileInputStream = getResources().openRawResource(R.raw.applist);
        String jsonStr = readTextFile(fileInputStream);
        Type mapType = new TypeToken<ArrayList<HashMap<String,String>>>(){}.getType();

        ArrayList<HashMap<String,String>> list=gson.fromJson(jsonStr,mapType);

        itemsAdapter=new ItemsAdapter(getActivity(),list);
        binding.mRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.mRecycleview.setAdapter(itemsAdapter);

    }
    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}