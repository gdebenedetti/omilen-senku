package com.omilen.games.senku.score;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;
import android.widget.TextView;

import com.omilen.games.senku.R;

public class HighScoreListItem extends TableLayout {
	private TextView mChips;
    private TextView mNames;
	
	public HighScoreListItem(Context context) {
		super(context);
	}
	
	public HighScoreListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mNames = (TextView) findViewById(R.id.score_name);
		mChips = (TextView) findViewById(R.id.score_chips);
        
	}
	
	public void init(ScoreItem score) {
		mNames.setText(score.getName());		
		int chips = score.getChips();
		mChips.setText(String.valueOf(chips));
	}	
}
