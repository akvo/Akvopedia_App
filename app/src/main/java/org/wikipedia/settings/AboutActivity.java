package org.wikipedia.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import org.wikipedia.BuildConfig;
import org.wikipedia.R;
import org.wikipedia.activity.ActivityUtil;
import org.wikipedia.activity.ThemedActionBarActivity;
import org.wikipedia.richtext.RichTextUtil;
import org.wikipedia.util.FeedbackUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.wikipedia.util.DeviceUtil.mailAppExists;

public class AboutActivity extends ThemedActionBarActivity {
    private static final String KEY_SCROLL_X = "KEY_SCROLL_X";
    private static final String KEY_SCROLL_Y = "KEY_SCROLL_Y";

    private ScrollView mScrollView;
    @BindView(R.id.about_translators) TextView translatorsTextView;
    @BindView(R.id.activity_about_libraries) TextView librariesTextView;
    @BindView(R.id.about_app_license) TextView appLicenseTextView;
    @BindView(R.id.send_feedback_text) TextView feedbackTextView;
    @BindView(R.id.about_wmf) TextView wmfTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        mScrollView = (ScrollView) findViewById(R.id.about_scrollview);
        translatorsTextView.setText(Html.fromHtml(getString(R.string.about_translators_translatewiki)));
        RichTextUtil.removeUnderlinesFromLinks(translatorsTextView);
        wmfTextView.setText(Html.fromHtml(getString(R.string.about_wmf)));
        RichTextUtil.removeUnderlinesFromLinks(wmfTextView);
        appLicenseTextView.setText(Html.fromHtml(getString(R.string.about_app_license)));
        RichTextUtil.removeUnderlinesFromLinks(appLicenseTextView);
        ((TextView) findViewById(R.id.about_version_text)).setText(BuildConfig.VERSION_NAME);
        feedbackTextView.setText(Html.fromHtml(
                "<a href=\"mailto:mobile-android-wikipedia@wikimedia.org?subject=Android App "
                + BuildConfig.VERSION_NAME
                + " Feedback\">"
                + getString(R.string.send_feedback)
                + "</a>"));
        RichTextUtil.removeUnderlinesFromLinks(feedbackTextView);
        RichTextUtil.removeUnderlinesFromLinks(librariesTextView);

        findViewById(R.id.about_logo_image).setOnClickListener(new AboutLogoClickListener(this));

        //if there's no Email app, hide the Feedback link.
        if (!mailAppExists(this)) {
            feedbackTextView.setVisibility(View.GONE);
        }

        makeEverythingClickable((ViewGroup) findViewById(R.id.about_container));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return ActivityUtil.defaultOnOptionsItemSelected(this, item)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SCROLL_X, mScrollView.getScrollX());
        outState.putInt(KEY_SCROLL_Y, mScrollView.getScrollY());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int x = savedInstanceState.getInt(KEY_SCROLL_X);
        final int y = savedInstanceState.getInt(KEY_SCROLL_Y);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(x, y);
            }
        });
    }

    @Override
    protected void setTheme() {
        setActionBarTheme();
    }

    private void makeEverythingClickable(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            if (vg.getChildAt(i) instanceof ViewGroup) {
                makeEverythingClickable((ViewGroup)vg.getChildAt(i));
            } else if (vg.getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) vg.getChildAt(i);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    private static class AboutLogoClickListener implements View.OnClickListener {
        private static final int SECRET_CLICK_LIMIT = 7;

        private final Activity mActivity;
        private int mSecretClickCount;

        AboutLogoClickListener(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void onClick(View v) {
            ++mSecretClickCount;
            if (isSecretClickLimitMet()) {
                if (Prefs.isShowDeveloperSettingsEnabled()) {
                    showSettingAlreadyEnabledMessage();
                } else {
                    Prefs.setShowDeveloperSettingsEnabled(true);
                    showSettingEnabledMessage();
                }
            }
        }

        private boolean isSecretClickLimitMet() {
            return mSecretClickCount == SECRET_CLICK_LIMIT;
        }

        private void showSettingEnabledMessage() {
            FeedbackUtil.showMessage(mActivity, R.string.show_developer_settings_enabled);
        }

        private void showSettingAlreadyEnabledMessage() {
            FeedbackUtil.showMessage(mActivity,
                    R.string.show_developer_settings_already_enabled);
        }
    }
}
