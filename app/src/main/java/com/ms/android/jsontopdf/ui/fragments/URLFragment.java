package com.ms.android.jsontopdf.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ms.android.jsontopdf.R;
import com.ms.android.jsontopdf.controller.JSONHandlerTask;
import com.ms.android.jsontopdf.utils.Utils;


public class URLFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private Context _context;
    private View urlView;
    private EditText etURL;
    private ImageButton ibCancel, ibURLGo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        urlView = inflater.inflate(R.layout.frag_url, container, false);
        initViews();
        setListeners();
        return urlView;
    }

    private void initViews() {
        etURL = (EditText) urlView.findViewById(R.id.etURL);
        ibCancel = (ImageButton) urlView.findViewById(R.id.ibCancel);
        ibURLGo = (ImageButton) urlView.findViewById(R.id.ibURLGo);
    }

    private void setListeners() {
        ibCancel.setOnClickListener(this);
        ibURLGo.setOnClickListener(this);
        etURL.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibCancel: {
                etURL.setText("");
                break;
            }
            case R.id.ibURLGo: {
                hideSoftKeyboard();
                submit();
                break;
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            hideSoftKeyboard();
            submit();
            return true;
        }
        return false;
    }

    private void callJSONTask(String url) {
            JSONHandlerTask jsonHandlerTask = new JSONHandlerTask(URLFragment.this);
            jsonHandlerTask.execute(url);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)_context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etURL.getWindowToken(), 0);
    }

    private void submit() {
        String url = etURL.getText().toString();
        etURL.setText(Utils.getInstance().makeURL(url));
        callJSONTask(etURL.getText().toString());
    }

    public void onJSONTaskResponse(String json) {
        if (!json.equals("")) {
            Utils.getInstance().showSnackbar(getActivity(),"Loading JSON ...","dismiss");
            callResponseFragment(json);
        }
    }

    private void callResponseFragment(String json) {
        ResponseFragment responseFragment = new ResponseFragment();

        Bundle data = new Bundle();
        data.putString(ResponseFragment.JSONDATA, json);
        responseFragment.setArguments(data);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, responseFragment)
                .setCustomAnimations(R.anim.enter_to_left, R.anim.exit_to_right)
                .addToBackStack(null).commit();
    }

}
