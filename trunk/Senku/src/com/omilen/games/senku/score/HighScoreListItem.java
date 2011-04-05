package com.omilen.games.senku.score;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.omilen.games.senku.R;

public class HighScoreListItem extends TableLayout {
	private TextView mPegs;
    private TextView mDate;
    private TextView mScore;
    private ImageView mPegView;
    private ImageView mBoardView;
    
    protected static Bitmap[] pegs = null;
    protected static Bitmap[] boards = null;
    
	public static void setBitmap(Bitmap[] ppegs,Bitmap[] pboards){
		pegs = ppegs;
		boards = pboards;		
	}
    
	public HighScoreListItem(Context context) {
		super(context);		
	}
	
		
	public HighScoreListItem(Context context, AttributeSet attrs) {
		super(context, attrs);		
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mDate = (TextView) findViewById(R.id.score_name);
		mPegs = (TextView) findViewById(R.id.score_pegs);
		mScore = (TextView) findViewById(R.id.score_score);
		mBoardView = (ImageView) findViewById(R.id.score_board);
		mPegView = (ImageView) findViewById(R.id.score_peg);       
	}
	
	public void init(ScoreItem score) {
		try{
		mDate.setText(score.getDate());
		mPegs.setText(String.valueOf(score.getPegs()));
		mScore.setText(String.valueOf(score.getScore()));
		mBoardView.setImageBitmap(boards[score.getGameNum()]);
		mPegView.setImageBitmap(pegs[score.getPegNum()]);
		}catch(Exception e){
			Log.d("Senku Exception-->", e.getMessage());
		}
	}	
}
