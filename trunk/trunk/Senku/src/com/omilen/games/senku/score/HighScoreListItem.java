package com.omilen.games.senku.score;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.omilen.games.senku.R;

public class HighScoreListItem extends TableLayout {
	private TextView mChips;
    private TextView mNames;
    private TextView mScore;
    private ImageView mPegView;
    private ImageView mBoardView;
	
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
		mScore = (TextView) findViewById(R.id.score_name);
//		mPegView = (ImageView) findViewById(R.id.peg_icon);
//		mBoardView = (ImageView) findViewById(R.id.board_icon);
			
        
	}
	
	public void init(ScoreItem score) {
		mNames.setText(score.getName());		
		int chips = score.getChips();
		mChips.setText(String.valueOf(chips));
	}	
}
