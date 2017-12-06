package org.wikipedia.settings;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import org.wikipedia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticesDialogFragment extends DialogFragment {

    public static NoticesDialogFragment newInstance() {
        return new NoticesDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notices_dialog, container, false);

//        try {
//            WebView webNotices = view.findViewById(R.id.webNotices);
//            webNotices.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            webNotices.loadUrl("file:///android_asset/open_source_licenses.html");
//        } catch (Exception ignored) {
//        }

        makeEverythingClickable((ViewGroup) view.findViewById(R.id.about_container));

        return view;
    }

    private void makeEverythingClickable(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            if (vg.getChildAt(i) instanceof ViewGroup) {
                makeEverythingClickable((ViewGroup) vg.getChildAt(i));
            } else if (vg.getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) vg.getChildAt(i);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    @Override
    public void onResume() {
        if (getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }

        super.onResume();
    }
}
