/*!
 * Copyright 2010-2011, Omilen IT Solutions
 * licensed under Apache Version 2.0, http://www.apache.org/licenses/
 * http://www.omilenitsolutions.com/
 * Author: Juan Manuel Rodríguez
 */
package com.omilen.games.senku.score;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.omilen.games.senku.R;

public class HighScoreListAdapter extends ArrayAdapter<ScoreItem> {
    
	protected Bitmap[] pegs = null;
	protected Bitmap[] boards = null;
	
	public HighScoreListAdapter(Context context, List<ScoreItem> scores, Bitmap[] ppegs, Bitmap[] pboards) {
    	super(context, R.layout.high_score_list_item, R.id.score_score, scores);
    	this.pegs = ppegs;
    	this.boards = pboards;
    }
    
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
    	HighScoreListItem listItem;
    	ScoreItem item = (ScoreItem) getItem(position);
    	HighScoreListItem.setBitmap(pegs,boards);
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
