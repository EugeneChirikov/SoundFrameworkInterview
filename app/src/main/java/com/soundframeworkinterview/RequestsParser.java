package com.soundframeworkinterview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Chirikov <chiricov.eugene@gmail.com>
 * @created 03/01/16
 */
public class RequestsParser {
    public static List<SearchResult> parseSearchResults(JSONObject response) {
        List<SearchResult> results = new ArrayList<>();
        try {
            JSONArray media = response.getJSONObject("rhos").getJSONArray("media");
            for (int i = 0; i < media.length(); ++i) {
                SearchResult result = new SearchResult();
                JSONObject song = media.getJSONObject(i);
                String id = song.getString("id");
                JSONObject meta = song.getJSONObject("meta");
                String artist = meta.getString("artist");
                String title = meta.getString("title");
                result.id = id;
                result.artist = artist;
                result.title = title;
                results.add(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }
}
