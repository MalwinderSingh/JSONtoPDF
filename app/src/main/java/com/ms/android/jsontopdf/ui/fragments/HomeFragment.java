package com.ms.android.jsontopdf.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ms.android.jsontopdf.R;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btnFromURL,btnFromFile;
    private View parent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_home, container, false);
        initViewData();
        setListeners();
        return parent;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFromFile: {
                callFileFragment();
                break;
            }
            case R.id.btnFromURL: {
                callURLFragment();
                break;
            }
        }
    }

    private void initViewData() {
        btnFromFile = (Button) parent.findViewById(R.id.btnFromFile);
        btnFromURL = (Button) parent.findViewById(R.id.btnFromURL);
    }

    private void setListeners() {
        btnFromURL.setOnClickListener(this);
        btnFromFile.setOnClickListener(this);
    }

    private void callFileFragment() {
        FileFragment fileFragment = new FileFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame,fileFragment)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_to_left, R.anim.exit_to_right)
                .commit();
    }

    private void callURLFragment() {
        URLFragment urlFragment = new URLFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame,urlFragment)
                .addToBackStack(null)
                .setCustomAnimations(R.anim.enter_to_left, R.anim.exit_to_right)
                .commit();
    }

}
