package org.wikipedia.page;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wikipedia.Site;

import static org.wikipedia.util.JsonUtil.jsonArrayToStringArray;
import static org.wikipedia.util.UriUtil.decodeURL;

public final class PageInfoUnmarshaller {
    @NonNull
    public static PageInfo unmarshal(@NonNull PageTitle title,
                                     @NonNull Site site,
                                     @NonNull JSONObject jsonObj) {
        DisambigResult[] disambiguations;
        String[] issues;
        try {
            disambiguations = parseDisambigJson(site, jsonObj.getJSONArray("disambiguations"));
            issues = jsonArrayToStringArray(jsonObj.getJSONArray("issues"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return new PageInfo(title, disambiguations, issues);
    }

    private static DisambigResult[] parseDisambigJson(Site site, JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }
        DisambigResult[] stringArray = new DisambigResult[array.length()];
        for (int i = 0; i < array.length(); i++) {
            stringArray[i] = new DisambigResult(site.titleForInternalLink(decodeURL(array.getString(i))));
        }
        return stringArray;
    }

    private PageInfoUnmarshaller() { }
}