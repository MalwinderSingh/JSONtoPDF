package com.ms.android.jsontopdf.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.android.jsontopdf.R;
import com.ms.android.jsontopdf.utils.Utils;

/**
 * Created by Malwinder on 24-10-2015.
 */
public class DrawerAdapter extends ArrayAdapter {

    private Context _context;

    public DrawerAdapter(Context context) {
        super(context,R.layout.drawer_list_item);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, parent,false);
        }

        ImageView ivDrawerItemIcon = (ImageView) convertView.findViewById(R.id.ivDrawerItemIcon);
        TextView tvDrawerItemTitle = (TextView) convertView.findViewById(R.id.tvDrawerItemTitle);

        switch (position) {
            case 0: {
                ivDrawerItemIcon.setImageResource(R.drawable.ic_url);
                ivDrawerItemIcon.setColorFilter(Utils.getInstance().getNegativeColorFilter());
                tvDrawerItemTitle.setText(_context.getString(R.string.from_url));
                break;
            }
            case 1: {
                ivDrawerItemIcon.setImageResource(R.drawable.ic_file);
                ivDrawerItemIcon.setColorFilter(Utils.getInstance().getNegativeColorFilter());
                tvDrawerItemTitle.setText(_context.getString(R.string.from_file));
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
