package com.ms.android.jsontopdf.controller;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.ms.android.jsontopdf.ui.fragments.URLFragment;
import com.ms.android.jsontopdf.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Malwinder on 24-10-2015.
 */
public class JSONHandlerTask extends AsyncTask<String, Void, String> {
    private Fragment fragment;

    public JSONHandlerTask(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Utils.getInstance().showProgressDialog(fragment.getActivity(), "Please wait ...", "Fetching data");
    }

    @Override
    protected String doInBackground(String... strings) {
        return Utils.getInstance().getFormattedJSON(getJSONString(strings[0]));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Utils.getInstance().dismissDialog();
        if (s.length() == 0) {
            Utils.getInstance().showSnackbar(fragment.getActivity(), "Invalid URL", "dismiss");
        }
        ((URLFragment) fragment).onJSONTaskResponse(s);
    }

    private String getJSONString(String url) {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL urlObj = new URL(url);

            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");

            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String message;
            while ((message = br.readLine()) != null) {
                message.concat("\n");
                sb.append(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return sb.toString();
    }
}