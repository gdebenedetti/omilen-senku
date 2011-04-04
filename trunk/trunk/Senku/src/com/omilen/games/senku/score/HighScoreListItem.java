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
    
	public static void setBitmap(Bitmap[] ppegs){
		pegs = ppegs;
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
		switch (score.getGameNum()) {
		case 0: mBoardView.setImageResource(R.drawable.ic_menu_board_00);
			break;
		case 1: mBoardView.setImageResource(R.drawable.ic_menu_board_01);
			break;
		case 2: mBoardView.setImageResource(R.drawable.ic_menu_board_02);
			break;
		case 3: mBoardView.setImageResource(R.drawable.ic_menu_board_03);
			break;
		case 4: mBoardView.setImageResource(R.drawable.ic_menu_board_04);
			break;
		case 5: mBoardView.setImageResource(R.drawable.ic_menu_board);
			break;
		case 6: mBoardView.setImageResource(R.drawable.ic_menu_board_06);
			break;
		}
		mPegView.setImageBitmap(pegs[score.getPegNum()]);
		}catch(Exception e){
			Log.d("Senku Exception-->", e.getMessage());
		}
	}	
}
