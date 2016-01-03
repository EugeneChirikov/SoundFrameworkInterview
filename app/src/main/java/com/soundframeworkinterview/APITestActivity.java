package com.soundframeworkinterview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APITestActivity extends AppCompatActivity {
    static final String SEARCH_REQUEST_TAG = "search_request";
    static final String SEARCH_URL =
            "http://alpha.core.soundframework.com/api/0/rest/json/media/find/query/";
    static final String AUTH_HEADER_KEY = "Authorization";
    static final String AUTH_HEADER_VALUE = "bearer client";
    RequestQueue mRequestQueue;
    SearchResultsAdapter mResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apitest);
        mRequestQueue = Volley.newRequestQueue(this);
        ListView resultsList = (ListView) findViewById(R.id.resultsList);
        mResultsAdapter = new SearchResultsAdapter(this);
        resultsList.setAdapter(mResultsAdapter);
        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResultsAdapter adapter = (SearchResultsAdapter) parent.getAdapter();
                SearchResult item = adapter.getItem(position);
                String formattedString = String.format("id=%s, artist=%s, title=%s",
                        item.id, item.artist, item.title);
                Toast.makeText(APITestActivity.this, formattedString, Toast.LENGTH_SHORT).show();
            }
        });
        SearchView searchView = (SearchView) findViewById(R.id.searchEdit);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                refreshResults(newText);
                return true;
            }
        });
    }

    private void refreshResults(String searchKey) {
        mRequestQueue.cancelAll(SEARCH_REQUEST_TAG);
        if (searchKey.isEmpty()) {
            mResultsAdapter.addItems(null);
            return;
        }
        String url;
        try {
            url = SEARCH_URL + URLEncoder.encode(searchKey, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(APITestActivity.this,
                    "Encoding error", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        updateListWith(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(APITestActivity.this,
                                "Network error", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTH_HEADER_KEY, AUTH_HEADER_VALUE);
                return headers;
            }
        };
        request.setTag(SEARCH_REQUEST_TAG);
        mRequestQueue.add(request);
    }

    private void updateListWith(JSONObject response) {
        List<SearchResult> results = RequestsParser.parseSearchResults(response);
        mResultsAdapter.addItems(results);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRequestQueue.cancelAll(SEARCH_REQUEST_TAG);
    }
}
