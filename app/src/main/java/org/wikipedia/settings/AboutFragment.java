package org.wikipedia.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wikipedia.BackPressedHandler;
import org.wikipedia.R;

import static org.wikipedia.util.DimenUtil.getContentTopOffsetPx;

/**
 * Activity to display info about the app.
 */
public class AboutFragment extends Fragment implements BackPressedHandler {

    /**
     * To display version number.
     */
    TextView textVersion;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        rootView.setPadding(0, getContentTopOffsetPx(getActivity()), 0, 0);

        TextView textVersion = rootView.findViewById(R.id.about_version_text);

        textVersion.setText(getAppVersion());

        TextView textNoticesLink = rootView.findViewById(R.id.link_software_notices_text);

        textNoticesLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoticesDialogFragment dialog = NoticesDialogFragment.newInstance();
                dialog.show(getChildFragmentManager(), "NoticesDialog");

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Gets the app version.
     *
     * @return The version name and number
     */
    public String getAppVersion() {
        String version = "";
        try {
            if (getActivity() != null) {
                PackageInfo packageInfo = getActivity().getApplication().getApplicationContext()
                        .getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);

                version = packageInfo.versionName;
            }

        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return version;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
