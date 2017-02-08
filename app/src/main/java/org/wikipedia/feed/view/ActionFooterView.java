package org.wikipedia.feed.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.wikipedia.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActionFooterView extends FrameLayout {
    @BindView(R.id.view_card_action_footer_button) View actionButton;
    @BindView(R.id.view_card_action_footer_button_icon) ImageView actionIcon;
    @BindView(R.id.view_card_action_footer_button_text) TextView actionText;
    @BindView(R.id.view_card_action_footer_share_button) View shareButton;

    public ActionFooterView(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_card_action_footer, this);
        ButterKnife.bind(this);
    }

    public ActionFooterView actionIcon(@DrawableRes int resId) {
        actionIcon.setImageResource(resId);
        return this;
    }

    public ActionFooterView actionText(@StringRes int resId) {
        actionText.setText(getResources().getString(resId));
        return this;
    }

    public ActionFooterView onActionListener(@Nullable OnClickListener listener) {
        actionButton.setOnClickListener(listener);
        return this;
    }

    public ActionFooterView onShareListener(@Nullable OnClickListener listener) {
        shareButton.setOnClickListener(listener);
        return this;
    }
}
