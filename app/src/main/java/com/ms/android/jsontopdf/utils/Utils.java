package com.ms.android.jsontopdf.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.itextpdf.text.Font;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Malwinder on 24-10-2015.
 */
public class Utils {
    private static Utils instance;
    private ProgressDialog ringProgressDialog;
    public Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);

    private Utils() {
    }

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public ColorFilter getNegativeColorFilter() {
        //To generate negative image
        float[] colorMatrix_Negative = {
                -1.0f, 0, 0, 0, 255, //red
                0, -1.0f, 0, 0, 255, //green
                0, 0, -1.0f, 0, 255, //blue
                0, 0, 0, 1.0f, 0 //alpha
        };

        return new ColorMatrixColorFilter(colorMatrix_Negative);
    }

    public String makeURL(String string) {
        if (string.startsWith("http://") || string.startsWith("https://")) {
            return string;
        } else {
            return "http://".concat(string);
        }
    }

    public void showSnackbar(Activity activity, String message, String action) {
        final Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        };

        snackbar.setAction(action.toUpperCase(), mOnClickListener)
                .setActionTextColor(Color.YELLOW)
                .show();
    }

    public SpannableStringBuilder getSpannableStringBuilder(String path) {
        path = "Your PDF is saved at " + path;
        SpannableStringBuilder sb = new SpannableStringBuilder(path);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, 20, path.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 20, path.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            try {
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getFormattedJSON(String json) {
        try {
            if (json.lastIndexOf("}") > json.lastIndexOf("]")) {
                return new JSONObject(json).toString(2);
            } else {
                return new JSONArray(json).toString(2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void showProgressDialog(Context _context, String title, String message) {
        ringProgressDialog = new ProgressDialog(_context);
        ringProgressDialog.setTitle(title);
        ringProgressDialog.setMessage(message);
        ringProgressDialog.setIndeterminate(true);
        ringProgressDialog.show();
    }

    public void dismissDialog() {
        ringProgressDialog.dismiss();
    }
}