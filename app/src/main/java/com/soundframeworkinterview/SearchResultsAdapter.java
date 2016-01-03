package com.soundframeworkinterview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugene Chirikov <chiricov.eugene@gmail.com>
 * @created 03/01/16
 */
public class SearchResultsAdapter extends BaseAdapter {
    List<SearchResult> mSearchResults = new ArrayList<>();
    Context mContext;
    LayoutInflater mInflater;

    public SearchResultsAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItems(List<SearchResult> results) {
        if (results == null) {
            mSearchResults.clear();
        } else {
            mSearchResults = results;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSearchResults.size();
    }

    @Override
    public SearchResult getItem(int position) {
        return mSearchResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView resultView = (TextView) convertView.findViewById(android.R.id.text1);
        SearchResult searchResult = mSearchResults.get(position);
        String formattedResult = String.format("%s - %s", searchResult.title, searchResult.artist);
        resultView.setText(formattedResult);
        return convertView;
    }
}
