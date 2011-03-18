package com.omilen.games.senku.score;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.omilen.games.senku.R;

public class HighScoreListAdapter extends ArrayAdapter<ScoreItem> {
    
	public HighScoreListAdapter(Context context, List<ScoreItem> scores) {
    	super(context, R.layout.high_score_list_item, R.id.score_chips, scores);
    }
    
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
    	HighScoreListItem listItem;
    	ScoreItem item = (ScoreItem) getItem(position);
    	
    	if (view == null) {
    		LayoutInflater factory = LayoutInflater.from(getContext());
    		listItem = (HighScoreListItem) factory.inflate(
    				R.layout.high_score_list_item, viewGroup, false);
    	} else {
    		if (view instanceof HighScoreListItem) {
    			listItem = (HighScoreListItem) view;
    		} else {
    			return view;
    		}
    	}
    	
    	listItem.init(item);
    	
    	return listItem;
    }
}
