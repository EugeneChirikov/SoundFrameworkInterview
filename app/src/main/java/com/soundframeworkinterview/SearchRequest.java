package com.soundframeworkinterview;

import com.android.volley.Response;
import com.soundframeworkinterview.models.SearchResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Chirikov <chiricov.eugene@gmail.com>
 * @created 05/01/16
 */
public class SearchRequest {
    public static final String TAG = "search_request";
    static final String URL =
            "http://alpha.core.soundframework.com/api/0/rest/json/media/find/query/";
    static final String AUTH_HEADER_KEY = "Authorization";
    static final String AUTH_HEADER_VALUE = "bearer client";
    GsonGetRequest<SearchResponse> mRequest;

    /**
     * Make a GET request and return a parsed SearchResult object from JSON.
     *
     * @param clazz         Relevant class object, for Gson's reflection
     * @param listener      Result callback
     * @param errorListener Error callback
     */
    public SearchRequest(Class<SearchResponse> clazz, String searchKey,
                         Response.Listener<SearchResponse> listener,
                         Response.ErrorListener errorListener
    ) throws UnsupportedEncodingException {
        String url = URL + URLEncoder.encode(searchKey, "utf-8");
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTH_HEADER_KEY, AUTH_HEADER_VALUE);
        mRequest = new GsonGetRequest<>(url, clazz, headers, listener, errorListener);
        mRequest.setTag(TAG);
    }

    public GsonGetRequest<SearchResponse> getVolleyRequest() {
        return mRequest;
    }
}
