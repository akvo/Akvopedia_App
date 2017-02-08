package org.wikipedia.database;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.wikipedia.database.contract.AppContentProviderContract;
import org.wikipedia.database.contract.EditHistoryContract;
import org.wikipedia.database.contract.PageHistoryContract;
import org.wikipedia.database.contract.PageImageHistoryContract;
import org.wikipedia.database.contract.ReadingListContract;
import org.wikipedia.database.contract.ReadingListPageContract;
import org.wikipedia.database.contract.SavedPageContract;
import org.wikipedia.database.contract.SearchHistoryContract;
import org.wikipedia.database.contract.UserOptionContract;
import org.wikipedia.model.EnumCode;
import org.wikipedia.model.EnumCodeMap;

public enum AppContentProviderEndpoint implements EnumCode {
    HISTORY_PAGE(100, PageHistoryContract.Page.PATH, PageHistoryContract.Page.TABLES,
            PageHistoryContract.Page.PROJECTION),
    HISTORY_PAGE_IMAGE(101, PageImageHistoryContract.Image.PATH,
            PageImageHistoryContract.Image.TABLES, PageImageHistoryContract.Image.PROJECTION),
    HISTORY_PAGE_WITH_IMAGE(102, PageHistoryContract.PageWithImage.PATH,
            PageHistoryContract.PageWithImage.TABLES, PageHistoryContract.PageWithImage.PROJECTION),
    HISTORY_EDIT_SUMMARY(103, EditHistoryContract.Summary.PATH, EditHistoryContract.Summary.TABLES,
            EditHistoryContract.Summary.PROJECTION),
    HISTORY_SEARCH_QUERY(104, SearchHistoryContract.Query.PATH, SearchHistoryContract.Query.TABLES,
            SearchHistoryContract.Query.PROJECTION),

    SAVED_PAGE(200, SavedPageContract.Page.PATH, SavedPageContract.Page.TABLES,
            SavedPageContract.Page.PROJECTION),
    SAVED_PAGE_WITH_IMAGE(201, SavedPageContract.PageWithImage.PATH,
            SavedPageContract.PageWithImage.TABLES, SavedPageContract.PageWithImage.PROJECTION),

    USER_OPTION(300, UserOptionContract.AUTHORITY, UserOptionContract.Option.PATH,
            UserOptionContract.Option.TABLES, UserOptionContract.Option.PROJECTION),
    USER_OPTION_HTTP(301, UserOptionContract.AUTHORITY, UserOptionContract.Http.PATH,
            UserOptionContract.Http.TABLES, UserOptionContract.Http.PROJECTION),
    USER_HTTP_WITH_OPTION(302, UserOptionContract.AUTHORITY, UserOptionContract.HttpWithOption.PATH,
            UserOptionContract.HttpWithOption.TABLES, UserOptionContract.HttpWithOption.PROJECTION),

    READING_LIST_PAGE(400, ReadingListPageContract.Page.PATH, ReadingListPageContract.Page.TABLES,
            ReadingListPageContract.Page.PROJECTION),
    READING_LIST_PAGE_HTTP(401, ReadingListPageContract.Http.PATH,
            ReadingListPageContract.Http.TABLES, ReadingListPageContract.Http.PROJECTION),
    READING_LIST_PAGE_DISK(402, ReadingListPageContract.Disk.PATH,
            ReadingListPageContract.Disk.TABLES, ReadingListPageContract.Disk.PROJECTION),
    READING_LIST_HTTP_WITH_PAGE(403, ReadingListPageContract.HttpWithPage.PATH,
            ReadingListPageContract.HttpWithPage.TABLES,
            ReadingListPageContract.HttpWithPage.PROJECTION),
    READING_LIST_DISK_WITH_PAGE(404, ReadingListPageContract.DiskWithPage.PATH,
            ReadingListPageContract.DiskWithPage.TABLES,
            ReadingListPageContract.DiskWithPage.PROJECTION),
    READING_LIST_PAGE_WITH_DISK(405, ReadingListPageContract.PageWithDisk.PATH,
            ReadingListPageContract.PageWithDisk.TABLES,
            ReadingListPageContract.PageWithDisk.PROJECTION),
    READING_LIST(406, ReadingListContract.List.PATH, ReadingListContract.List.TABLES,
            ReadingListContract.List.PROJECTION),
    READING_LIST_WITH_PAGES_AND_DISK(407, ReadingListContract.ListWithPagesAndDisk.PATH,
            ReadingListContract.ListWithPagesAndDisk.TABLES,
            ReadingListContract.ListWithPagesAndDisk.PROJECTION);

    private static final EnumCodeMap<AppContentProviderEndpoint> CODE_TO_ENUM = new EnumCodeMap<>(AppContentProviderEndpoint.class);
    private static final UriMatcher URI_TO_CODE = newUriToCode();

    private final int code;
    @NonNull private final String authority;
    @NonNull private final String path;
    @NonNull private final String tables;
    @Nullable private final String[] projection;

    @NonNull public static AppContentProviderEndpoint of(@NonNull Uri uri) {
        int code = URI_TO_CODE.match(uri);
        if (code == UriMatcher.NO_MATCH) {
            throw new IllegalArgumentException("uri=" + uri);
        }

        return of(code);
    }

    @Override public int code() {
        return code;
    }

    @NonNull public String tables() {
        return tables;
    }

    @Nullable public String[] projection() {
        return projection;
    }

    @Nullable public String type() {
        return null;
    }

    @Nullable public Uri itemUri(@NonNull ContentValues values) {
        return null;
    }

    AppContentProviderEndpoint(int code, @NonNull String path, @NonNull String tables,
                               @Nullable String[] projection) {
        this(code, AppContentProviderContract.AUTHORITY, path, tables, projection);
    }

    AppContentProviderEndpoint(int code, @NonNull String authority, @NonNull String path,
                               @NonNull String tables, @Nullable String[] projection) {
        this.code = code;
        this.authority = authority;
        this.path = path;
        this.tables = tables;
        this.projection = projection;
    }

    @NonNull private static AppContentProviderEndpoint of(int code) {
        return CODE_TO_ENUM.get(code);
    }

    private static UriMatcher newUriToCode() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        for (AppContentProviderEndpoint value : AppContentProviderEndpoint.values()) {
            matcher.addURI(value.authority, value.path, value.code);
        }
        return matcher;
    }
}
