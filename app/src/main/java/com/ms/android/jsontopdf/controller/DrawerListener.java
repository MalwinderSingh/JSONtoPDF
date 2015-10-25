package com.ms.android.jsontopdf.controller;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ms.android.jsontopdf.R;
import com.ms.android.jsontopdf.ui.fragments.FileFragment;
import com.ms.android.jsontopdf.ui.fragments.URLFragment;

/**
 * Created by Malwinder on 24-10-2015.
 */
public class DrawerListener implements ListView.OnItemClickListener {

    private AppCompatActivity _activity;
    private DrawerLayout mDrawerLayout;

    public DrawerListener(AppCompatActivity _activity, DrawerLayout mDrawerLayout) {
        this._activity = _activity;
        this.mDrawerLayout = mDrawerLayout;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {
        _activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, position == 0 ? new URLFragment() : new FileFragment())
                .setCustomAnimations(R.anim.enter_to_left, R.anim.exit_to_right).commit();

        mDrawerLayout.closeDrawers();
    }
}
