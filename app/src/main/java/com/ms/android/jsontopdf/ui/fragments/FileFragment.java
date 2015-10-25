package com.ms.android.jsontopdf.ui.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ms.android.jsontopdf.R;
import com.ms.android.jsontopdf.utils.Utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FileFragment extends Fragment implements View.OnClickListener {

    private Button btnFileGo;
    private View parent;

    private static final int FILE_SELECT_CODE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.frag_file, container, false);
        initViews();
        setListeners();
        return parent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    String path = Utils.getInstance().getPath(getActivity(), uri);
                    if(path.toLowerCase().endsWith(".json")) {
                        Utils.getInstance().showProgressDialog(getActivity(), "Loading ...", "Loading JSON file ...");
                        String json = Utils.getInstance().getFormattedJSON(getJSON(path));
                        callResponseFragment(json);
                    }
                    else {
                        Utils.getInstance().showSnackbar(getActivity(),"Selected file is not JSON file","dismiss");
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFileGo: {
                showFileChooser();
                break;
            }
        }
    }

    private void initViews() {
        btnFileGo = (Button) parent.findViewById(R.id.btnFileGo);
    }

    private void setListeners() {
        btnFileGo.setOnClickListener(this);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (ActivityNotFoundException ex) {
            Utils.getInstance().showSnackbar(getActivity(), "File manager not found", "dismiss");
        }
    }

    private String getJSON(String filePath) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            int size = bufferedInputStream.available();
            byte[] buffer = new byte[size];
            bufferedInputStream.read(buffer);
            bufferedInputStream.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void callResponseFragment(String formattedJSON) {
        ResponseFragment responseFragment = new ResponseFragment();
        Bundle data = new Bundle();
        data.putString(ResponseFragment.JSONDATA, formattedJSON);
        responseFragment.setArguments(data);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_frame, responseFragment)
                .setCustomAnimations(R.anim.enter_to_left, R.anim.exit_to_right)
                .commit();
    }
}
