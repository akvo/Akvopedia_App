package org.wikipedia.page;

import org.wikipedia.R;
import org.wikipedia.MainActivity;
import org.wikipedia.history.HistoryEntry;
import org.wikipedia.readinglist.AddToReadingListDialog;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import static org.wikipedia.util.L10nUtil.getStringForArticleLanguage;

/**
 * A dialog to host page issues and disambig information.
 */
public class PageInfoDialog extends NoDimBottomSheetDialog {
    private final ViewFlipper flipper;
    private final Button disambigHeading;
    private final Button issuesHeading;
    private final ListView disambigList;

    public PageInfoDialog(final MainActivity activity, PageInfo pageInfo, boolean startAtDisambig) {
        super(activity);
        View parentView = LayoutInflater.from(activity).inflate(R.layout.dialog_page_info, null);
        setContentView(parentView);

        flipper = (ViewFlipper) parentView.findViewById(R.id.page_info_flipper);
        disambigList = (ListView) parentView.findViewById(R.id.disambig_list);
        ListView issuesList = (ListView) parentView.findViewById(R.id.page_issues_list);
        disambigHeading = (Button) parentView.findViewById(R.id.page_info_similar_titles_heading);
        issuesHeading = (Button) parentView.findViewById(R.id.page_info_page_issues_heading);
        View separatorHeading = parentView.findViewById(R.id.page_info_heading_separator);
        View closeButton = parentView.findViewById(R.id.page_info_close);

        disambigHeading.setText(getStringForArticleLanguage(pageInfo.getTitle(), R.string.page_similar_titles));
        issuesHeading.setText(getStringForArticleLanguage(pageInfo.getTitle(), R.string.dialog_page_issues));

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        issuesList.setAdapter(new IssuesListAdapter(activity, pageInfo.getContentIssues()));
        disambigList.setAdapter(new DisambigListAdapter(activity, pageInfo.getSimilarTitles()));
        disambigList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PageTitle title = ((DisambigResult) disambigList.getAdapter().getItem(position)).getTitle();
                HistoryEntry historyEntry = new HistoryEntry(title, HistoryEntry.SOURCE_DISAMBIG);
                dismiss();
                activity.loadPage(title, historyEntry);
            }
        });
        PageLongPressHandler.ListViewContextMenuListener contextMenuListener = new LongPressHandler(activity);
        new PageLongPressHandler(getContext(), disambigList, HistoryEntry.SOURCE_DISAMBIG,
                contextMenuListener);

        if (pageInfo.getSimilarTitles().length > 0) {
            disambigHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDisambig();
                }
            });
        } else {
            disambigHeading.setVisibility(View.GONE);
            separatorHeading.setVisibility(View.GONE);
        }
        if (pageInfo.getContentIssues().length > 0) {
            issuesHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showIssues();
                }
            });
        } else {
            issuesHeading.setVisibility(View.GONE);
            separatorHeading.setVisibility(View.GONE);
        }

        if (startAtDisambig) {
            showDisambig();
        } else {
            showIssues();
        }
    }

    private void showDisambig() {
        startExpanded();
        if (flipper.getCurrentView() != flipper.getChildAt(0)) {
            flipper.setInAnimation(getContext(), R.anim.slide_in_left);
            flipper.setOutAnimation(getContext(), R.anim.slide_out_right);
            flipper.showNext();
        }

        disambigHeading.setTypeface(null, Typeface.BOLD);
        disambigHeading.setEnabled(false);
        issuesHeading.setTypeface(null, Typeface.NORMAL);
        issuesHeading.setEnabled(true);
    }

    private void showIssues() {
        if (flipper.getCurrentView() != flipper.getChildAt(1)) {
            flipper.setInAnimation(getContext(), R.anim.slide_in_right);
            flipper.setOutAnimation(getContext(), R.anim.slide_out_left);
            flipper.showPrevious();
        }

        disambigHeading.setTypeface(null, Typeface.NORMAL);
        disambigHeading.setEnabled(true);
        issuesHeading.setTypeface(null, Typeface.BOLD);
        issuesHeading.setEnabled(false);
    }

    private class LongPressHandler extends MainActivityLongPressHandler
            implements PageLongPressHandler.ListViewContextMenuListener {
        LongPressHandler(@NonNull MainActivity activity) {
            super(activity);
        }

        @Override
        public PageTitle getTitleForListPosition(int position) {
            return ((DisambigResult) disambigList.getAdapter().getItem(position)).getTitle();
        }

        @Override
        public void onOpenLink(PageTitle title, HistoryEntry entry) {
            super.onOpenLink(title, entry);
            dismiss();
        }

        @Override
        public void onOpenInNewTab(PageTitle title, HistoryEntry entry) {
            super.onOpenInNewTab(title, entry);
            dismiss();
        }

        @Override
        public void onCopyLink(PageTitle title) {
            super.onCopyLink(title);
            dismiss();
        }

        @Override
        public void onShareLink(PageTitle title) {
            super.onShareLink(title);
            dismiss();
        }

        @Override
        public void onAddToList(PageTitle title, AddToReadingListDialog.InvokeSource source) {
            super.onAddToList(title, source);
        }
    }
}
