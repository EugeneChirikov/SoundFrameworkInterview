package com.soundframeworkinterview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.soundframeworkinterview.models.MediaItem;
import com.soundframeworkinterview.models.SearchResponse;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class APITestActivity extends AppCompatActivity {
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
                MediaItem item = adapter.getItem(position);
                String formattedString = String.format("id=%s, artist=%s, title=%s",
                        item.id, item.meta.artist, item.meta.title);
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
        mRequestQueue.cancelAll(SearchRequest.TAG);
        if (searchKey.isEmpty()) {
            mResultsAdapter.addItems((List<MediaItem>)null);
            return;
        }
        SearchRequest request;
        try {
            request = new SearchRequest(SearchResponse.class, searchKey,
                    new Response.Listener<SearchResponse>() {
                        @Override
                        public void onResponse(SearchResponse response) {
                            mResultsAdapter.addItems(response.rhos.media);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(APITestActivity.this,
                                    "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(APITestActivity.this,
                    "Encoding error", Toast.LENGTH_SHORT).show();
            return;
        }
        mRequestQueue.add(request.getVolleyRequest());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRequestQueue.cancelAll(SearchRequest.TAG);
    }
}
