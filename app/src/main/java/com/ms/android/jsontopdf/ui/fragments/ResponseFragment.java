package com.ms.android.jsontopdf.ui.fragments;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ms.android.jsontopdf.R;
import com.ms.android.jsontopdf.controller.PDFGeneratorTask;
import com.ms.android.jsontopdf.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

public class ResponseFragment extends Fragment implements View.OnClickListener, OnClickListener {

    private TextView tvJSONResp;
    private Button btnConvToPDF;
    private View parent;
    private File file;
    public static final String JSONDATA = "JSONDATA";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_response, container, false);

        initViews();
        setListeners();
        setViewData();

        return parent;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConvToPDF: {
                if (!Utils.getInstance().isExternalStorageWritable()) {
                    Utils.getInstance().showSnackbar(getActivity(), "Unable to write to external storage", "dismiss");
                    Log.i("JSON TO PDF", "Cannot write to external storage");
                    break;
                }
                file = getFile();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (fileOutputStream != null) {
                    PDFGeneratorTask pdfGenerator = new PDFGeneratorTask(ResponseFragment.this, getArguments().getString(JSONDATA));
                    pdfGenerator.execute(fileOutputStream);
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE: {
                startPDFIntent();
                break;
            }
            case DialogInterface.BUTTON_NEGATIVE: {
                dialogInterface.dismiss();
                break;
            }
        }
    }

    private void initViews() {
        tvJSONResp = (TextView) parent.findViewById(R.id.tvJSONResp);
        btnConvToPDF = (Button) parent.findViewById(R.id.btnConvToPDF);
    }

    private void setListeners() {
        btnConvToPDF.setOnClickListener(this);
    }

    private void setViewData() {
        tvJSONResp.setText(getArguments().getString(JSONDATA));
        Utils.getInstance().dismissDialog();
    }

    private File getFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "JSON " + Calendar.getInstance().getTimeInMillis() + ".pdf");
        } else {
            return new File(Environment.getExternalStorageDirectory(), "JSON " + Calendar.getInstance().getTimeInMillis() + ".pdf");
        }
    }

    public void showFileDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setTitle("File Saved");
        builder.setMessage(Utils.getInstance().getSpannableStringBuilder(file.getAbsolutePath()));
        builder.setPositiveButton("Open", this);
        builder.setNegativeButton("Cancel", this);
        builder.show();
    }

    private void startPDFIntent() {
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Utils.getInstance().showSnackbar(getActivity(), "You do not have application to open PDF", "dismiss");
        }
    }
}
